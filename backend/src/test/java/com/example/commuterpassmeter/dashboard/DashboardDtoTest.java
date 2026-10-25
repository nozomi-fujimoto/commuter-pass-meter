package com.example.commuterpassmeter.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.commuterpassmeter.attendance.AttendanceCommuterPassId;
import com.example.commuterpassmeter.attendance.AttendanceCreatedAt;
import com.example.commuterpassmeter.attendance.AttendanceDate;
import com.example.commuterpassmeter.attendance.AttendanceRecord;
import com.example.commuterpassmeter.attendance.AttendanceRecordId;
import com.example.commuterpassmeter.attendance.AttendanceUserId;
import com.example.commuterpassmeter.attendance.RoundTripFare;
import com.example.commuterpassmeter.commuterpass.CommuterPass;
import com.example.commuterpassmeter.commuterpass.FareAmount;
import com.example.commuterpassmeter.commuterpass.PassMemo;
import com.example.commuterpassmeter.commuterpass.PassPeriod;
import com.example.commuterpassmeter.commuterpass.StationName;
import com.fasterxml.jackson.databind.ObjectMapper;

class DashboardDtoTest {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void commuterPassDtoPreservesExistingJsonIncludingOptionalMemo() {
        LocalDateTime now = LocalDateTime.of(2026, 10, 25, 9, 0);
        for (String memo : new String[] {null, "JR定期"}) {
            CommuterPass pass = new CommuterPass(1L, 1L, new StationName("横浜"), new StationName("品川"),
                    new FareAmount(310), new FareAmount(14200),
                    new PassPeriod(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31)),
                    new PassMemo(memo), true, now, now);
            assertEquals(mapper.valueToTree(pass), mapper.valueToTree(CommuterPassResponse.from(pass)));
        }
    }

    @Test
    void recentAttendanceDtoPreservesExistingJson() {
        AttendanceRecord record = new AttendanceRecord(new AttendanceRecordId(1L), new AttendanceUserId(1L),
                new AttendanceCommuterPassId(2L), new AttendanceDate(LocalDate.of(2026, 10, 25)),
                new RoundTripFare(620), new AttendanceCreatedAt(LocalDateTime.of(2026, 10, 25, 9, 0)));
        assertEquals(mapper.valueToTree(record), mapper.valueToTree(RecentAttendanceRecordResponse.from(record)));
    }
}
