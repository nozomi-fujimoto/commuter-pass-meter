package com.example.commuterpassmeter.attendance;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 出社記録の所有ユーザーを表すIDです。
 */
public record AttendanceUserId(Long value) {

    public AttendanceUserId {
        if (value == null) {
            throw new InvalidAttendanceRecordException("userId is required");
        }
        if (value < 1) {
            throw new InvalidAttendanceRecordException("userId must be greater than 0");
        }
    }

    @JsonValue
    @Override
    public Long value() {
        return value;
    }
}
