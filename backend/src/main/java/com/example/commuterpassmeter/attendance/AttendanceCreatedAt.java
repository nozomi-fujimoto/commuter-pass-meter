package com.example.commuterpassmeter.attendance;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 出社記録を作成した日時です。
 */
public record AttendanceCreatedAt(LocalDateTime value) {

    public AttendanceCreatedAt {
        if (value == null) {
            throw new InvalidAttendanceRecordException("createdAt is required");
        }
    }

    @JsonValue
    @Override
    public LocalDateTime value() {
        return value;
    }
}
