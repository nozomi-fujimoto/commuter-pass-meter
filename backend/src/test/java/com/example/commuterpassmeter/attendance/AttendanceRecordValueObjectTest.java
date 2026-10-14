package com.example.commuterpassmeter.attendance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * 出社記録関連の値オブジェクトの入力制約を確認するテストです。
 */
class AttendanceRecordValueObjectTest {

    @Test
    void rejectsInvalidIds() {
        assertThatThrownBy(() -> new AttendanceRecordId(null))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("id is required");
        assertThatThrownBy(() -> new AttendanceUserId(0L))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("userId must be greater than 0");
        assertThatThrownBy(() -> new AttendanceCommuterPassId(0L))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("commuterPassId must be greater than 0");
    }

    @Test
    void rejectsMissingDateAndCreatedAt() {
        assertThatThrownBy(() -> new AttendanceDate(null))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("attendanceDate is required");
        assertThatThrownBy(() -> new AttendanceCreatedAt(null))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("createdAt is required");
    }

    @Test
    void rejectsInvalidRoundTripFare() {
        assertThatThrownBy(() -> new RoundTripFare(null))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("roundTripFare is required");
        assertThatThrownBy(() -> new RoundTripFare(0))
                .isInstanceOf(InvalidAttendanceRecordException.class)
                .hasMessage("roundTripFare must be greater than 0");
    }

    @Test
    void acceptsValidValues() {
        new AttendanceRecordId(1L);
        new AttendanceUserId(1L);
        new AttendanceCommuterPassId(1L);
        new AttendanceDate(LocalDate.parse("2026-09-23"));
        new RoundTripFare(620);
        new AttendanceCreatedAt(LocalDateTime.parse("2026-09-23T09:00:00"));
    }
}
