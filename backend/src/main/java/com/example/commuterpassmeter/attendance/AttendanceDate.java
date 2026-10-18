package com.example.commuterpassmeter.attendance;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 実際に出社した日付です。
 */
public record AttendanceDate(LocalDate value) {

    public AttendanceDate {
        if (value == null) {
            throw new InvalidAttendanceRecordException("attendanceDate is required");
        }
    }

    @JsonValue
    @Override
    public LocalDate value() {
        return value;
    }
}
