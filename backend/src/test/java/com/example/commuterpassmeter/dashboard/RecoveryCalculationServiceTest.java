package com.example.commuterpassmeter.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.commuterpassmeter.commuterpass.FareAmount;

class RecoveryCalculationServiceTest {

    private final RecoveryCalculationService service = new RecoveryCalculationService();

    @ParameterizedTest
    @CsvSource({"12,7440,-6760,52.4,11", "23,14260,60,100.4,0", "0,0,-14200,0.0,23"})
    void calculatesAcceptanceExamples(int count, long total, long difference, String rate, long remaining) {
        RecoveryCalculationService.RecoverySummary result = service.calculate(new FareAmount(310), new FareAmount(14200), new AttendanceCount(count));
        assertEquals(620, result.roundTripFare().value());
        assertEquals(total, result.normalFareTotal().value());
        assertEquals(difference, result.difference().value());
        assertEquals(new BigDecimal(rate), result.recoveryRate().value());
        assertEquals(23, result.breakEvenAttendanceCount().value());
        assertEquals(remaining, result.remainingAttendanceCount().value());
    }

    @Test
    void handlesExactBreakEvenAndLaterAttendance() {
        RecoveryCalculationService.RecoverySummary exact = service.calculate(new FareAmount(100), new FareAmount(600), new AttendanceCount(3));
        assertEquals(3, exact.breakEvenAttendanceCount().value());
        assertEquals(0, exact.difference().value());
        assertEquals(new BigDecimal("100.0"), exact.recoveryRate().value());
        assertEquals(0, exact.remainingAttendanceCount().value());
        assertEquals(0, service.calculate(new FareAmount(100), new FareAmount(600), new AttendanceCount(4))
                .remainingAttendanceCount().value());
    }

    @Test
    void avoidsIntegerOverflowForLargeAmounts() {
        RecoveryCalculationService.RecoverySummary result = service.calculate(new FareAmount(1073741823), new FareAmount(1), new AttendanceCount(Integer.MAX_VALUE));
        assertEquals(4611686011984936962L, result.normalFareTotal().value());
        assertEquals(4611686011984936961L, result.difference().value());
        assertEquals(1, result.breakEvenAttendanceCount().value());
        assertEquals(0, result.remainingAttendanceCount().value());
    }

    @Test
    void rejectsNegativeAttendanceCount() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(new FareAmount(310), new FareAmount(14200), new AttendanceCount(-1)));
    }
}
