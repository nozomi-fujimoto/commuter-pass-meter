package com.example.commuterpassmeter.attendance;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.commuterpassmeter.commuterpass.CommuterPass;
import com.example.commuterpassmeter.commuterpass.CommuterPassNotFoundException;
import com.example.commuterpassmeter.commuterpass.CommuterPassRepository;

/**
 * 出社履歴の取得、記録の作成と今日の記録状態の取得を担当するサービスです。
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

    public List<AttendanceRecordResponse> getHistory(LocalDate from, LocalDate to) {
        return historyRecords(from, to)
                .sorted(Comparator.comparing((AttendanceRecord record) -> record.attendanceDate().value()).reversed())
                .map(AttendanceRecordResponse::from)
                .toList();
    }

    public List<MonthlyAttendanceResponse> getMonthlySummary(LocalDate from, LocalDate to) {
        return historyRecords(from, to)
                .collect(Collectors.groupingBy(
                        record -> YearMonth.from(record.attendanceDate().value()),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .map(entry -> new MonthlyAttendanceResponse(entry.getKey().toString(), entry.getValue()))
                .toList();
    }

    private Stream<AttendanceRecord> historyRecords(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new InvalidAttendanceRecordException("from must be on or before to");
        }
        return attendanceRecordRepository.findByUserId(new AttendanceUserId(FIXED_USER_ID)).stream()
                .filter(record -> from == null || !record.attendanceDate().value().isBefore(from))
                .filter(record -> to == null || !record.attendanceDate().value().isAfter(to));
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
