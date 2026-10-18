package com.example.commuterpassmeter.commuterpass;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 定期券料金または片道運賃の金額です。
 */
public record FareAmount(Integer value) {

    public FareAmount {
        if (value == null) {
            throw new InvalidCommuterPassException("amount is required");
        }
        if (value > Integer.MAX_VALUE / 2) {
            throw new InvalidCommuterPassException("amount must be at most " + Integer.MAX_VALUE / 2);
        }
        if (value < 1) {
            throw new InvalidCommuterPassException("amount must be at least 1");
        }
    }

    @JsonValue
    @Override
    public Integer value() {
        return value;
    }
}
