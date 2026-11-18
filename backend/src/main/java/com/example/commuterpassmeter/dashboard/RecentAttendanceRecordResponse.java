package com.example.commuterpassmeter.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.commuterpassmeter.attendance.AttendanceRecord;

/** Dashboardに公開する出社履歴。既存のJSON項目を明示的に定義します。 */
public record RecentAttendanceRecordResponse(Long id, Long userId, Long commuterPassId,
        LocalDate attendanceDate, int roundTripFare, LocalDateTime createdAt) {
    public static RecentAttendanceRecordResponse from(AttendanceRecord record) {
        return new RecentAttendanceRecordResponse(record.id().value(), record.userId().value(),
                record.commuterPassId().value(), record.attendanceDate().value(),
                record.roundTripFare().value(), record.createdAt().value());
    }
}
