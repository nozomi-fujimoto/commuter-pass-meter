package com.example.commuterpassmeter.attendance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 出社記録APIの正常系と異常系を確認するテストです。
 */
@SpringBootTest
@AutoConfigureMockMvc
class AttendanceRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsTodayAttendanceRecordAndRejectsDuplicateDate() throws Exception {
        createCommuterPass("2026-09-01", "2026-09-30");

        mockMvc.perform(post("/api/attendance-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "attendanceDate": "2026-09-24"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.attendanceDate").value("2026-09-24"))
                .andExpect(jsonPath("$.roundTripFare").value(620));

        mockMvc.perform(post("/api/attendance-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "attendanceDate": "2026-09-24"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Attendance record already exists for the date"));
    }

    @Test
    void rejectsAttendanceDateOutsideCommuterPassPeriod() throws Exception {
        createCommuterPass("2026-10-01", "2026-10-31");

        mockMvc.perform(post("/api/attendance-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "attendanceDate": "2026-11-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("attendanceDate must be within the commuter pass period"));
    }

    @Test
    void returnsTodayRecordedState() throws Exception {
        createCommuterPass("2026-09-01", "2026-09-30");

        mockMvc.perform(get("/api/attendance-records/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recorded").value(false));
    }

    private void createCommuterPass(String startDate, String endDate) throws Exception {
        mockMvc.perform(post("/api/commuter-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromStation": "横浜",
                                  "toStation": "品川",
                                  "oneWayFare": 310,
                                  "passPrice": 14200,
                                  "startDate": "%s",
                                  "endDate": "%s",
                                  "memo": "JR定期"
                                }
                                """.formatted(startDate, endDate)))
                .andExpect(status().isCreated());
    }
}
