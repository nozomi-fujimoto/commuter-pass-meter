package com.example.commuterpassmeter.attendance;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 出社記録を一意に識別するIDです。
 */
public record AttendanceRecordId(Long value) {

    public AttendanceRecordId {
        if (value == null) {
            throw new InvalidAttendanceRecordException("id is required");
        }
        if (value < 1) {
            throw new InvalidAttendanceRecordException("id must be greater than 0");
        }
    }

    @JsonValue
    @Override
    public Long value() {
        return value;
    }
}
