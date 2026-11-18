package com.example.commuterpassmeter.dashboard;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.commuterpassmeter.attendance.AttendanceCommuterPassId;
import com.example.commuterpassmeter.attendance.AttendanceRecord;
import com.example.commuterpassmeter.attendance.AttendanceRecordRepository;
import com.example.commuterpassmeter.attendance.AttendanceUserId;
import com.example.commuterpassmeter.commuterpass.CommuterPassService;
import com.example.commuterpassmeter.commuterpass.CommuterPass;

/** 有効な定期に紐づく出社記録からDashboardを組み立てます。 */
@Service
public class DashboardService {

    private static final int RECENT_RECORD_LIMIT = 5;

    private final CommuterPassService commuterPassService;
    private final AttendanceRecordRepository attendanceRepository;
    private final RecoveryCalculationService calculationService;
    private final Clock clock;

    public DashboardService(CommuterPassService commuterPassService,
            AttendanceRecordRepository attendanceRepository,
            RecoveryCalculationService calculationService, Clock clock) {
        this.commuterPassService = commuterPassService;
        this.attendanceRepository = attendanceRepository;
        this.calculationService = calculationService;
        this.clock = clock;
    }

    public DashboardResponse get() {
        CommuterPass pass = commuterPassService.getActive();
        List<AttendanceRecord> records = attendanceRepository.findByUserIdAndCommuterPassId(
                new AttendanceUserId(pass.userId()), new AttendanceCommuterPassId(pass.id()));
        AttendanceCount attendanceCount = new AttendanceCount(records.size());
        RecoveryCalculationService.RecoverySummary summary = calculationService.calculate(pass.oneWayFare(), pass.passPrice(), attendanceCount);
        LocalDate today = LocalDate.now(clock);
        YearMonth month = YearMonth.from(today);
        AttendanceCount monthlyCount = new AttendanceCount(Math.toIntExact(records.stream()
                .filter(record -> YearMonth.from(record.attendanceDate().value()).equals(month))
                .count()));
        boolean hasAttendedToday = records.stream()
                .anyMatch(record -> record.attendanceDate().value().equals(today));
        List<RecentAttendanceRecordResponse> recentRecords = records.stream()
                .sorted(Comparator.comparing((AttendanceRecord record) -> record.attendanceDate().value()).reversed())
                .limit(RECENT_RECORD_LIMIT)
                .map(RecentAttendanceRecordResponse::from)
                .toList();
        return new DashboardResponse(CommuterPassResponse.from(pass), attendanceCount.value(), monthlyCount.value(),
                summary.normalFareTotal().value(),
                summary.difference().value(), summary.recoveryRate().value(), summary.breakEvenAttendanceCount().value(),
                summary.remainingAttendanceCount().value(), hasAttendedToday, recentRecords);
    }
}
