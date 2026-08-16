package com.example.commuterpassmeter.commuterpass;

import com.fasterxml.jackson.annotation.JsonValue;

public record FareAmount(Integer value) {

    public FareAmount {
        if (value == null || value < 1) {
            throw new InvalidCommuterPassException("fare amount must be at least 1");
        }
    }

    @JsonValue
    @Override
    public Integer value() {
        return value;
    }
}
