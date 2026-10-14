package com.example.commuterpassmeter.attendance;

import java.util.Optional;

/**
 * 出社記録の永続化と検索を抽象化するRepositoryです。
 */
public interface AttendanceRecordRepository {
    Optional<AttendanceRecord> findByUserIdAndAttendanceDate(AttendanceUserId userId, AttendanceDate attendanceDate);

    AttendanceRecord save(AttendanceRecord attendanceRecord);
}
