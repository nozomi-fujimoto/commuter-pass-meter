package com.example.commuterpassmeter.attendance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * 出社記録Repositoryの重複制約を確認するテストです。
 */
class InMemoryAttendanceRecordRepositoryTest {

    @Test
    void savingDuplicateDateForSameUserIsRejected() {
        InMemoryAttendanceRecordRepository repository = new InMemoryAttendanceRecordRepository();
        AttendanceRecord saved = repository.save(record(null, 1L, LocalDate.parse("2026-09-23")));

        assertThat(saved.id()).isEqualTo(new AttendanceRecordId(1L));
        assertThatThrownBy(() -> repository.save(record(null, 1L, LocalDate.parse("2026-09-23"))))
                .isInstanceOf(DuplicateAttendanceRecordException.class)
                .hasMessage("Attendance record already exists for the date");
    }

    @Test
    void sameDateForDifferentUserIsAllowed() {
        InMemoryAttendanceRecordRepository repository = new InMemoryAttendanceRecordRepository();
        repository.save(record(null, 1L, LocalDate.parse("2026-09-23")));

        AttendanceRecord saved = repository.save(record(null, 2L, LocalDate.parse("2026-09-23")));

        assertThat(saved.id()).isEqualTo(new AttendanceRecordId(2L));
        assertThat(saved.userId()).isEqualTo(new AttendanceUserId(2L));
    }

    private AttendanceRecord record(Long id, Long userId, LocalDate attendanceDate) {
        return new AttendanceRecord(
                id == null ? null : new AttendanceRecordId(id),
                new AttendanceUserId(userId),
                new AttendanceCommuterPassId(1L),
                new AttendanceDate(attendanceDate),
                new RoundTripFare(620),
                new AttendanceCreatedAt(LocalDateTime.parse("2026-09-23T09:00:00")));
    }
}
