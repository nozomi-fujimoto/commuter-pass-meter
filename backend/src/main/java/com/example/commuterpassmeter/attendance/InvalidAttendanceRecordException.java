package com.example.commuterpassmeter.attendance;

/**
 * 出社記録の入力値または状態が不正な場合の例外です。
 */
public class InvalidAttendanceRecordException extends RuntimeException {
    public InvalidAttendanceRecordException(String message) {
        super(message);
    }
}
