package com.example.commuterpassmeter.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 出社履歴一覧APIのレスポンスです。 */
public record AttendanceRecordResponse(
        long id,
        long commuterPassId,
        LocalDate attendanceDate,
        int roundTripFare,
        LocalDateTime createdAt) {

    public static AttendanceRecordResponse from(AttendanceRecord record) {
        return new AttendanceRecordResponse(
                record.id().value(),
                record.commuterPassId().value(),
                record.attendanceDate().value(),
                record.roundTripFare().value(),
                record.createdAt().value());
    }
}
