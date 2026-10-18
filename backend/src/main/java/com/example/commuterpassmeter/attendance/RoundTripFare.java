package com.example.commuterpassmeter.attendance;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 出社1回分の往復運賃です。
 */
public record RoundTripFare(Integer value) {

    public RoundTripFare {
        if (value == null) {
            throw new InvalidAttendanceRecordException("roundTripFare is required");
        }
        if (value < 1) {
            throw new InvalidAttendanceRecordException("roundTripFare must be greater than 0");
        }
    }

    @JsonValue
    @Override
    public Integer value() {
        return value;
    }
}
