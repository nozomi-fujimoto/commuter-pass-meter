package com.example.commuterpassmeter.commuterpass;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommuterPass(
        Long id,
        Long userId,
        StationName fromStation,
        StationName toStation,
        FareAmount oneWayFare,
        FareAmount passPrice,
        @JsonIgnore PassPeriod period,
        PassMemo memo,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    @JsonProperty("startDate")
    public LocalDate startDate() {
        return period.startDate();
    }

    @JsonProperty("endDate")
    public LocalDate endDate() {
        return period.endDate();
    }
}
