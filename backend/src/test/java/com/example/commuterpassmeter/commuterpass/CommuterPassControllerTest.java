package com.example.commuterpassmeter.commuterpass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CommuterPassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsGetsAndUpdatesCommuterPass() throws Exception {
        String createResponse = mockMvc.perform(post("/api/commuter-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromStation": "横浜",
                                  "toStation": "品川",
                                  "oneWayFare": 310,
                                  "passPrice": 14200,
                                  "startDate": "2026-08-01",
                                  "endDate": "2026-08-31",
                                  "memo": "JR定期"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.fromStation").value("横浜"))
                .andExpect(jsonPath("$.toStation").value("品川"))
                .andExpect(jsonPath("$.oneWayFare").value(310))
                .andExpect(jsonPath("$.passPrice").value(14200))
                .andExpect(jsonPath("$.startDate").value("2026-08-01"))
                .andExpect(jsonPath("$.endDate").value("2026-08-31"))
                .andExpect(jsonPath("$.active").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.valueOf(createResponse.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(get("/api/commuter-pass"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.memo").value("JR定期"));

        mockMvc.perform(put("/api/commuter-pass/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromStation": "武蔵小杉",
                                  "toStation": "東京",
                                  "oneWayFare": 410,
                                  "passPrice": 18800,
                                  "startDate": "2026-09-01",
                                  "endDate": "2026-09-30",
                                  "memo": "更新後"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fromStation").value("武蔵小杉"))
                .andExpect(jsonPath("$.toStation").value("東京"))
                .andExpect(jsonPath("$.oneWayFare").value(410))
                .andExpect(jsonPath("$.passPrice").value(18800))
                .andExpect(jsonPath("$.startDate").value("2026-09-01"))
                .andExpect(jsonPath("$.endDate").value("2026-09-30"))
                .andExpect(jsonPath("$.memo").value("更新後"));
    }

    @Test
    void rejectsInvalidInput() throws Exception {
        mockMvc.perform(post("/api/commuter-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromStation": "",
                                  "toStation": "品川",
                                  "oneWayFare": 0,
                                  "passPrice": 0,
                                  "startDate": "2026-09-30",
                                  "endDate": "2026-09-01"
                                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("station name is required"));
    }

    @Test
    void rejectsDateRangeWhenStartDateIsAfterEndDate() throws Exception {
        mockMvc.perform(post("/api/commuter-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromStation": "横浜",
                                  "toStation": "品川",
                                  "oneWayFare": 310,
                                  "passPrice": 14200,
                                  "startDate": "2026-09-30",
                                  "endDate": "2026-09-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("startDate must be on or before endDate"));
    }
}
