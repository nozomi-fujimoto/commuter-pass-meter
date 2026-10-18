package com.example.commuterpassmeter.attendance;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * 出社記録をメモリ上に保持するRepository実装です。
 */
@Repository
class InMemoryAttendanceRecordRepository implements AttendanceRecordRepository {

    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<AttendanceRecordId, AttendanceRecord> records = new LinkedHashMap<>();

    @Override
    public synchronized Optional<AttendanceRecord> findByUserIdAndAttendanceDate(
            AttendanceUserId userId,
            AttendanceDate attendanceDate) {
        return records.values().stream()
                .filter(record -> record.userId().equals(userId))
                .filter(record -> record.attendanceDate().equals(attendanceDate))
                .findFirst();
    }

    @Override
    public synchronized AttendanceRecord save(AttendanceRecord attendanceRecord) {
        findByUserIdAndAttendanceDate(attendanceRecord.userId(), attendanceRecord.attendanceDate())
                .filter(existing -> !existing.id().equals(attendanceRecord.id()))
                .ifPresent(existing -> {
                    throw new DuplicateAttendanceRecordException("Attendance record already exists for the date");
                });

        AttendanceRecordId id = attendanceRecord.id() == null
                ? new AttendanceRecordId(sequence.getAndIncrement())
                : attendanceRecord.id();
        AttendanceRecord saved = new AttendanceRecord(
                id,
                attendanceRecord.userId(),
                attendanceRecord.commuterPassId(),
                attendanceRecord.attendanceDate(),
                attendanceRecord.roundTripFare(),
                attendanceRecord.createdAt());
        records.put(id, saved);
        return saved;
    }
}
