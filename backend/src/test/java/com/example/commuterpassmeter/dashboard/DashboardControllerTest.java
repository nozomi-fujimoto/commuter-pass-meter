package com.example.commuterpassmeter.dashboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.example.commuterpassmeter.attendance.AttendanceCommuterPassId;
import com.example.commuterpassmeter.attendance.AttendanceCreatedAt;
import com.example.commuterpassmeter.attendance.AttendanceDate;
import com.example.commuterpassmeter.attendance.AttendanceRecord;
import com.example.commuterpassmeter.attendance.AttendanceRecordRepository;
import com.example.commuterpassmeter.attendance.AttendanceUserId;
import com.example.commuterpassmeter.attendance.RoundTripFare;
import com.example.commuterpassmeter.commuterpass.CommuterPass;
import com.example.commuterpassmeter.commuterpass.CommuterPassRequest;
import com.example.commuterpassmeter.commuterpass.CommuterPassService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DashboardControllerTest.FixedClockConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DashboardControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CommuterPassService passService;
    @Autowired private AttendanceRecordRepository records;

    @TestConfiguration
    static class FixedClockConfiguration {
        @Bean
        @Primary
        Clock testClock() {
            // UTCでは前日でも、実行環境のタイムゾーンにおける今日を使用する。
            return Clock.fixed(Instant.parse("2026-10-24T16:00:00Z"), ZoneId.of("Asia/Tokyo"));
        }
    }

    @Test
    void returnsNotFoundWithoutAnActivePass() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Active commuter pass was not found"));
    }

    @Test
    void returnsZeroAttendanceSummary() throws Exception {
        createPass();
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commuterPass.fromStation").value("横浜"))
                .andExpect(jsonPath("$.commuterPass.oneWayFare").value(310))
                .andExpect(jsonPath("$.attendanceCount").value(0))
                .andExpect(jsonPath("$.monthlyAttendanceCount").value(0))
                .andExpect(jsonPath("$.normalFareTotal").value(0))
                .andExpect(jsonPath("$.difference").value(-14200))
                .andExpect(jsonPath("$.recoveryRate").value(0.0))
                .andExpect(jsonPath("$.breakEvenAttendanceCount").value(23))
                .andExpect(jsonPath("$.remainingAttendanceCount").value(23))
                .andExpect(jsonPath("$.hasAttendedToday").value(false))
                .andExpect(jsonPath("$.recentAttendanceRecords").isEmpty());
    }

    @Test
    void aggregatesOnlyCurrentPassAndUserAndReturnsLatestFiveByAttendanceDate() throws Exception {
        CommuterPass pass = createPass();
        // 登録順と日付順を変え、月を跨ぐ記録を作る。
        for (int day = 6; day >= 1; day--) {
            save(1, pass.id(), LocalDate.of(2026, 10, day));
            save(1, pass.id(), LocalDate.of(2026, 9, 24 + day));
        }
        save(2, pass.id(), LocalDate.of(2026, 10, 25));
        save(1, pass.id() + 1, LocalDate.of(2026, 10, 25));

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceCount").value(12))
                .andExpect(jsonPath("$.monthlyAttendanceCount").value(6))
                .andExpect(jsonPath("$.normalFareTotal").value(7440))
                .andExpect(jsonPath("$.difference").value(-6760))
                .andExpect(jsonPath("$.recoveryRate").value(52.4))
                .andExpect(jsonPath("$.breakEvenAttendanceCount").value(23))
                .andExpect(jsonPath("$.remainingAttendanceCount").value(11))
                .andExpect(jsonPath("$.hasAttendedToday").value(false))
                .andExpect(jsonPath("$.recentAttendanceRecords.length()").value(5))
                .andExpect(jsonPath("$.recentAttendanceRecords[0].attendanceDate").value("2026-10-06"))
                .andExpect(jsonPath("$.recentAttendanceRecords[4].attendanceDate").value("2026-10-02"))
                .andExpect(jsonPath("$.recentAttendanceRecords[0].roundTripFare").value(620));
    }

    @Test
    void creatingTodayAttendanceUpdatesTotalsAndShowsRecoveredState() throws Exception {
        CommuterPass pass = createPass();
        for (int day = 1; day <= 22; day++) {
            save(1, pass.id(), LocalDate.of(2026, 10, day));
        }
        mockMvc.perform(post("/api/attendance-records")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.attendanceDate").value("2026-10-25"));
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceCount").value(23))
                .andExpect(jsonPath("$.monthlyAttendanceCount").value(23))
                .andExpect(jsonPath("$.normalFareTotal").value(14260))
                .andExpect(jsonPath("$.difference").value(60))
                .andExpect(jsonPath("$.recoveryRate").value(100.4))
                .andExpect(jsonPath("$.remainingAttendanceCount").value(0))
                .andExpect(jsonPath("$.hasAttendedToday").value(true))
                .andExpect(jsonPath("$.recentAttendanceRecords[0].attendanceDate").value("2026-10-25"));
    }

    private CommuterPass createPass() {
        return passService.create(new CommuterPassRequest("横浜", "品川", 310, 14200,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 31), null));
    }

    private void save(long userId, long passId, LocalDate date) {
        records.save(new AttendanceRecord(null, new AttendanceUserId(userId),
                new AttendanceCommuterPassId(passId), new AttendanceDate(date), new RoundTripFare(620),
                new AttendanceCreatedAt(LocalDateTime.of(2026, 10, 25, 9, 0))));
    }
}
