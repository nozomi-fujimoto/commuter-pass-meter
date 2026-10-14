package com.example.commuterpassmeter.attendance;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 出社記録に紐づく定期券IDです。
 */
public record AttendanceCommuterPassId(Long value) {

    public AttendanceCommuterPassId {
        if (value == null) {
            throw new InvalidAttendanceRecordException("commuterPassId is required");
        }
        if (value < 1) {
            throw new InvalidAttendanceRecordException("commuterPassId must be greater than 0");
        }
    }

    @JsonValue
    @Override
    public Long value() {
        return value;
    }
}
