package com.example.commuterpassmeter.commuterpass;

import java.time.LocalDate;

/**
 * 定期券の有効期間です。
 */
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

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
