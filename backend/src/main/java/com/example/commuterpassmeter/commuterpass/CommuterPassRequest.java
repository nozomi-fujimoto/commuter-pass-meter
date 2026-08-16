package com.example.commuterpassmeter.commuterpass;

import java.time.LocalDate;

public record CommuterPassRequest(
        String fromStation,
        String toStation,
        Integer oneWayFare,
        Integer passPrice,
        LocalDate startDate,
        LocalDate endDate,
        String memo) {
}
