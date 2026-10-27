package com.example.commuterpassmeter.dashboard;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.commuterpassmeter.attendance.RoundTripFare;
import com.example.commuterpassmeter.commuterpass.FareAmount;

/** 定期料金に対する回収状況を、副作用なく計算します。 */
@Service
public class RecoveryCalculationService {

    public RecoverySummary calculate(FareAmount oneWayFare, FareAmount passPrice, AttendanceCount attendanceCount) {
        Objects.requireNonNull(oneWayFare, "oneWayFare");
        Objects.requireNonNull(passPrice, "passPrice");
        Objects.requireNonNull(attendanceCount, "attendanceCount");
        RoundTripFare roundTripFare = new RoundTripFare(oneWayFare.value() * 2);
        NormalFareTotal normalFareTotal = NormalFareTotal.calculate(roundTripFare, attendanceCount);
        AttendanceCount breakEvenAttendanceCount = new AttendanceCount((int)
                ((passPrice.value() + (long) roundTripFare.value() - 1) / roundTripFare.value()));
        return new RecoverySummary(roundTripFare, normalFareTotal, normalFareTotal.differenceFrom(passPrice),
                RecoveryRate.calculate(normalFareTotal, passPrice), breakEvenAttendanceCount,
                breakEvenAttendanceCount.remainingAfter(attendanceCount));
    }

    /** 計算結果の各値は、それぞれの業務ルールを持つ値オブジェクトで表します。 */
    public record RecoverySummary(RoundTripFare roundTripFare, NormalFareTotal normalFareTotal,
            FareDifference difference, RecoveryRate recoveryRate, AttendanceCount breakEvenAttendanceCount,
            AttendanceCount remainingAttendanceCount) {
    }
}
