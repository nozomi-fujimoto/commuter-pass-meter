package com.example.commuterpassmeter.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.commuterpassmeter.commuterpass.FareAmount;

class DashboardValueObjectTest {

    @Test
    void rejectsNegativeCountsAndTotalsButAllowsZero() {
        assertThrows(IllegalArgumentException.class, () -> new AttendanceCount(-1));
        assertThrows(IllegalArgumentException.class, () -> new NormalFareTotal(-1));
        assertEquals(0, new AttendanceCount(0).value());
        assertEquals(0L, new NormalFareTotal(0).value());
        assertEquals(0, new AttendanceCount(3).remainingAfter(new AttendanceCount(4)).value());
    }

    @ParameterizedTest
    @CsvSource({"0,0.0", "52.34,52.3", "52.35,52.4", "100.45,100.5"})
    void roundsRatesAndAllowsMoreThanOneHundredPercent(String input, String expected) {
        assertEquals(new BigDecimal(expected), new RecoveryRate(new BigDecimal(input)).value());
    }

    @Test
    void rejectsNullAndNegativeRatesBeforeRounding() {
        assertThrows(IllegalArgumentException.class, () -> new RecoveryRate(null));
        assertThrows(IllegalArgumentException.class, () -> new RecoveryRate(new BigDecimal("-0.01")));
    }

    @Test
    void determinesRecoveryFromExactDifferenceRatherThanRoundedRate() {
        NormalFareTotal total = new NormalFareTotal(9999);
        FareAmount price = new FareAmount(10000);
        assertEquals(new BigDecimal("100.0"), RecoveryRate.calculate(total, price).value());
        assertFalse(total.differenceFrom(price).isRecovered());
        assertEquals(-1, total.differenceFrom(price).value());
        assertTrue(new FareDifference(0).isRecovered());
        assertTrue(new FareDifference(1).isRecovered());
    }
}
