package com.example.commuterpassmeter.dashboard;

/** 0以上の出社回数です。 */
public record AttendanceCount(int value) {
    public AttendanceCount {
        if (value < 0) {
            throw new IllegalArgumentException("attendanceCount must be at least 0");
        }
    }

    public AttendanceCount remainingAfter(AttendanceCount attended) {
        return new AttendanceCount(Math.max(value - attended.value, 0));
    }
}
