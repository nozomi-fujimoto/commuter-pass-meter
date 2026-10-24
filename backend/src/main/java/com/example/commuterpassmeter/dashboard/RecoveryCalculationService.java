package com.example.commuterpassmeter.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.commuterpassmeter.commuterpass.FareAmount;

/** 定期料金に対する回収状況を、副作用なく計算します。 */
@Service
public class RecoveryCalculationService {

    public RecoverySummary calculate(FareAmount oneWayFare, FareAmount passPrice, int attendanceCount) {
        Objects.requireNonNull(oneWayFare, "oneWayFare");
        Objects.requireNonNull(passPrice, "passPrice");
        if (attendanceCount < 0) {
            throw new IllegalArgumentException("attendanceCount must be at least 0");
        }
        long roundTripFare = oneWayFare.value() * 2L;
        long normalFareTotal = attendanceCount * roundTripFare;
        long difference = normalFareTotal - passPrice.value();
        BigDecimal recoveryRate = BigDecimal.valueOf(normalFareTotal)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(passPrice.value()), 1, RoundingMode.HALF_UP);
        long breakEvenAttendanceCount = (passPrice.value() + roundTripFare - 1) / roundTripFare;
        return new RecoverySummary(roundTripFare, normalFareTotal, difference, recoveryRate,
                breakEvenAttendanceCount, Math.max(breakEvenAttendanceCount - attendanceCount, 0));
    }

    /** 金額の累計にはlongを使い、回収率は小数第1位に四捨五入します。 */
    public record RecoverySummary(long roundTripFare, long normalFareTotal, long difference,
            BigDecimal recoveryRate, long breakEvenAttendanceCount, long remainingAttendanceCount) {
    }
}
