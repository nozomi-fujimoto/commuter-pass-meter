package com.example.commuterpassmeter.attendance;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.commuterpassmeter.commuterpass.CommuterPass;
import com.example.commuterpassmeter.commuterpass.CommuterPassNotFoundException;
import com.example.commuterpassmeter.commuterpass.CommuterPassRepository;

/**
 * 出社記録の作成と今日の記録状態の取得を担当するサービスです。
 */
@Service
public class AttendanceRecordService {

    private static final long FIXED_USER_ID = 1L;

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final CommuterPassRepository commuterPassRepository;
    private final Clock clock;

    public AttendanceRecordService(
            AttendanceRecordRepository attendanceRecordRepository,
            CommuterPassRepository commuterPassRepository,
            Clock clock) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.commuterPassRepository = commuterPassRepository;
        this.clock = clock;
    }

    public TodayAttendanceResponse getToday() {
        LocalDate today = LocalDate.now(clock);
        return attendanceRecordRepository.findByUserIdAndAttendanceDate(
                        new AttendanceUserId(FIXED_USER_ID),
                        new AttendanceDate(today))
                .map(record -> new TodayAttendanceResponse(true, record))
                .orElseGet(() -> new TodayAttendanceResponse(false, null));
    }

    public AttendanceRecord create(AttendanceRecordRequest request) {
        CommuterPass commuterPass = commuterPassRepository.findActiveByUserId(FIXED_USER_ID)
                .orElseThrow(() -> new CommuterPassNotFoundException("Active commuter pass was not found"));
        LocalDate attendanceDate = request.attendanceDate() == null ? LocalDate.now(clock) : request.attendanceDate();

        if (!commuterPass.period().contains(attendanceDate)) {
            throw new InvalidAttendanceRecordException("attendanceDate must be within the commuter pass period");
        }

        return attendanceRecordRepository.save(new AttendanceRecord(
                null,
                new AttendanceUserId(FIXED_USER_ID),
                new AttendanceCommuterPassId(commuterPass.id()),
                new AttendanceDate(attendanceDate),
                new RoundTripFare(commuterPass.oneWayFare().value() * 2),
                new AttendanceCreatedAt(LocalDateTime.now(clock))));
    }
}
