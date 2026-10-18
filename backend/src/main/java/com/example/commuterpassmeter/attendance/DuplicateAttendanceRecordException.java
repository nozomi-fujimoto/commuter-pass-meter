package com.example.commuterpassmeter.attendance;

/**
 * 同一ユーザー、同一日付の出社記録が既に存在する場合の例外です。
 */
public class DuplicateAttendanceRecordException extends RuntimeException {
    public DuplicateAttendanceRecordException(String message) {
        super(message);
    }
}
