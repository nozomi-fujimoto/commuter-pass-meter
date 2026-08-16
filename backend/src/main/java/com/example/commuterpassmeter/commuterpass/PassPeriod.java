package com.example.commuterpassmeter.commuterpass;

import java.time.LocalDate;

public record PassPeriod(LocalDate startDate, LocalDate endDate) {

    public PassPeriod {
        if (startDate == null) {
            throw new InvalidCommuterPassException("startDate is required");
        }
        if (endDate == null) {
            throw new InvalidCommuterPassException("endDate is required");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidCommuterPassException("startDate must be on or before endDate");
        }
    }
}
