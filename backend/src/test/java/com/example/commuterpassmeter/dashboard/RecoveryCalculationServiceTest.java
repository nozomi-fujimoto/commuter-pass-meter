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
        var result = service.calculate(new FareAmount(310), new FareAmount(14200), count);
        assertEquals(620, result.roundTripFare());
        assertEquals(total, result.normalFareTotal());
        assertEquals(difference, result.difference());
        assertEquals(new BigDecimal(rate), result.recoveryRate());
        assertEquals(23, result.breakEvenAttendanceCount());
        assertEquals(remaining, result.remainingAttendanceCount());
    }

    @Test
    void handlesExactBreakEvenAndLaterAttendance() {
        var exact = service.calculate(new FareAmount(100), new FareAmount(600), 3);
        assertEquals(3, exact.breakEvenAttendanceCount());
        assertEquals(0, exact.difference());
        assertEquals(new BigDecimal("100.0"), exact.recoveryRate());
        assertEquals(0, exact.remainingAttendanceCount());
        assertEquals(0, service.calculate(new FareAmount(100), new FareAmount(600), 4)
                .remainingAttendanceCount());
    }

    @Test
    void avoidsIntegerOverflowForLargeAmounts() {
        var result = service.calculate(new FareAmount(1073741823), new FareAmount(1), Integer.MAX_VALUE);
        assertEquals(4611686011984936962L, result.normalFareTotal());
        assertEquals(4611686011984936961L, result.difference());
        assertEquals(1, result.breakEvenAttendanceCount());
        assertEquals(0, result.remainingAttendanceCount());
    }

    @Test
    void rejectsNegativeAttendanceCount() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(new FareAmount(310), new FareAmount(14200), -1));
    }
}
