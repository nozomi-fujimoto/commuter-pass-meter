package com.example.commuterpassmeter.attendance;

import java.time.LocalDate;

/**
 * 出社記録作成APIのリクエストです。
 */
public record AttendanceRecordRequest(LocalDate attendanceDate) {
}
