package com.example.commuterpassmeter.attendance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.application.name=attendance-history-test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AttendanceHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttendanceRecordRepository repository;

    @Test
    void returnsEmptyHistoryWithoutAnActivePass() throws Exception {
        mockMvc.perform(get("/api/attendance-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void returnsAllUserPassesInDescendingDateOrderWithStoredFares() throws Exception {
        save(1, 10, "2026-09-01", 620);
        save(2, 20, "2026-11-01", 900);
        AttendanceRecord latest = save(1, 11, "2026-10-10", 700);
        mockMvc.perform(get("/api/attendance-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(latest.id().value()))
                .andExpect(jsonPath("$[0].commuterPassId").value(11))
                .andExpect(jsonPath("$[0].attendanceDate").value("2026-10-10"))
                .andExpect(jsonPath("$[0].roundTripFare").value(700))
                .andExpect(jsonPath("$[0].createdAt").value("2026-10-10T08:30:00"))
                .andExpect(jsonPath("$[0].userId").doesNotExist())
                .andExpect(jsonPath("$[1].attendanceDate").value("2026-09-01"))
                .andExpect(jsonPath("$[1].roundTripFare").value(620));
    }

    @Test
    void filtersInclusiveBoundsAndAllowsEachBoundIndependently() throws Exception {
        save(1, 10, "2026-10-01", 620);
        save(1, 10, "2026-10-10", 620);
        save(1, 10, "2026-10-20", 620);
        mockMvc.perform(get("/api/attendance-records").param("from", "2026-10-10").param("to", "2026-10-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].attendanceDate").value("2026-10-10"));
        mockMvc.perform(get("/api/attendance-records").param("from", "2026-10-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].attendanceDate").value("2026-10-20"));
        mockMvc.perform(get("/api/attendance-records").param("to", "2026-10-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].attendanceDate").value("2026-10-01"));
        mockMvc.perform(get("/api/attendance-records").param("from", "2026-11-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void groupsHistoryByYearAndMonthAcrossPassesAndFiltersBeforeCounting() throws Exception {
        save(1, 10, "2025-01-10", 620);
        save(1, 10, "2025-12-31", 620);
        save(1, 10, "2026-01-01", 620);
        save(1, 11, "2026-01-02", 700);
        save(2, 20, "2026-01-01", 900);
        String endpoint = "/api/attendance-records/monthly-summary";

        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].month").value("2026-01"))
                .andExpect(jsonPath("$[0].attendanceCount").value(2))
                .andExpect(jsonPath("$[1].month").value("2025-12"))
                .andExpect(jsonPath("$[1].attendanceCount").value(1))
                .andExpect(jsonPath("$[2].month").value("2025-01"))
                .andExpect(jsonPath("$[2].attendanceCount").value(1));
        mockMvc.perform(get(endpoint).param("from", "2025-12-31").param("to", "2026-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].attendanceCount").value(1))
                .andExpect(jsonPath("$[1].attendanceCount").value(1));
        mockMvc.perform(get(endpoint).param("from", "2026-02-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void rejectsInvalidMonthlySummaryRanges() throws Exception {
        String endpoint = "/api/attendance-records/monthly-summary";
        mockMvc.perform(get(endpoint).param("from", "2026-02-01").param("to", "2026-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("from must be on or before to"));
        mockMvc.perform(get(endpoint).param("to", "2026-13-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsReversedRangeAndInvalidDates() throws Exception {
        mockMvc.perform(get("/api/attendance-records").param("from", "2026-10-20").param("to", "2026-10-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("from must be on or before to"));
        mockMvc.perform(get("/api/attendance-records").param("from", "invalid"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/attendance-records").param("to", "2026-13-01"))
                .andExpect(status().isBadRequest());
    }

    private AttendanceRecord save(long userId, long passId, String date, int fare) {
        LocalDate attendanceDate = LocalDate.parse(date);
        LocalDateTime createdAt = attendanceDate.atTime(8, 30);
        return repository.save(new AttendanceRecord(null, new AttendanceUserId(userId),
                new AttendanceCommuterPassId(passId), new AttendanceDate(attendanceDate),
                new RoundTripFare(fare), new AttendanceCreatedAt(createdAt)));
    }
}
