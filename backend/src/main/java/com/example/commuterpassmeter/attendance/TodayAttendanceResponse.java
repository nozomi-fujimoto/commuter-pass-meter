package com.example.commuterpassmeter.attendance;

/**
 * 今日の出社記録が存在するかを表すレスポンスです。
 */
public record TodayAttendanceResponse(boolean recorded, AttendanceRecord record) {
}
