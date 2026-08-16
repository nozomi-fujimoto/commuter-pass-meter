package com.example.commuterpassmeter.commuterpass;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

class CommuterPassJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void serializesValueObjectsAsApiPrimitives() throws Exception {
        CommuterPass commuterPass = new CommuterPass(
                1L,
                1L,
                new StationName("横浜"),
                new StationName("品川"),
                new FareAmount(310),
                new FareAmount(14200),
                new PassPeriod(LocalDate.parse("2026-08-01"), LocalDate.parse("2026-08-31")),
                new PassMemo("JR定期"),
                true,
                LocalDateTime.parse("2026-08-01T09:00:00"),
                LocalDateTime.parse("2026-08-01T09:00:00"));

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(commuterPass));

        assertThat(json.get("fromStation").asText()).isEqualTo("横浜");
        assertThat(json.get("toStation").asText()).isEqualTo("品川");
        assertThat(json.get("oneWayFare").asInt()).isEqualTo(310);
        assertThat(json.get("passPrice").asInt()).isEqualTo(14200);
        assertThat(json.get("startDate").asText()).isEqualTo("2026-08-01");
        assertThat(json.get("endDate").asText()).isEqualTo("2026-08-31");
        assertThat(json.has("period")).isFalse();
    }
}
