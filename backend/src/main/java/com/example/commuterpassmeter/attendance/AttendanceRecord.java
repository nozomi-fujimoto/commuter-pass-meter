package com.example.commuterpassmeter.attendance;

/**
 * 出社1回分の記録を表すモデルです。
 */
public record AttendanceRecord(
        AttendanceRecordId id,
        AttendanceUserId userId,
        AttendanceCommuterPassId commuterPassId,
        AttendanceDate attendanceDate,
        RoundTripFare roundTripFare,
        AttendanceCreatedAt createdAt) {
}
