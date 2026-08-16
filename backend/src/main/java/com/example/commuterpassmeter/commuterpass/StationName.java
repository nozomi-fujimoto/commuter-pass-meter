package com.example.commuterpassmeter.commuterpass;

import com.fasterxml.jackson.annotation.JsonValue;

public record StationName(String value) {

    private static final int MAX_LENGTH = 80;

    public StationName {
        if (value == null || value.isBlank()) {
            throw new InvalidCommuterPassException("station name is required");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCommuterPassException("station name must be 80 characters or less");
        }
    }

    @JsonValue
    @Override
    public String value() {
        return value;
    }
}
