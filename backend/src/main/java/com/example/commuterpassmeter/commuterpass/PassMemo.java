package com.example.commuterpassmeter.commuterpass;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 定期券情報に添える任意メモです。
 */
public record PassMemo(String value) {

    private static final int MAX_LENGTH = 200;

    public PassMemo {
        if (value == null || value.isBlank()) {
            value = null;
        } else {
            value = value.trim();
            if (value.length() > MAX_LENGTH) {
                throw new InvalidCommuterPassException("memo must be 200 characters or less");
            }
        }
    }

    @JsonValue
    @Override
    public String value() {
        return value;
    }
}
