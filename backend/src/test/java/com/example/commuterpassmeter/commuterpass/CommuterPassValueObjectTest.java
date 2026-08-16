package com.example.commuterpassmeter.commuterpass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CommuterPassValueObjectTest {

    @Test
    void stationNameTrimsValue() {
        assertThat(new StationName(" 横浜 ").value()).isEqualTo("横浜");
    }

    @Test
    void stationNameRejectsBlankValue() {
        assertThatThrownBy(() -> new StationName(" "))
                .isInstanceOf(InvalidCommuterPassException.class)
                .hasMessage("station name is required");
    }

    @Test
    void fareAmountRejectsZero() {
        assertThatThrownBy(() -> new FareAmount(0))
                .isInstanceOf(InvalidCommuterPassException.class)
                .hasMessage("amount must be at least 1");
    }

    @Test
    void fareAmountRejectsNull() {
        assertThatThrownBy(() -> new FareAmount(null))
                .isInstanceOf(InvalidCommuterPassException.class)
                .hasMessage("amount is required");
    }

    @Test
    void passPeriodRejectsStartDateAfterEndDate() {
        assertThatThrownBy(() -> new PassPeriod(LocalDate.parse("2026-09-30"), LocalDate.parse("2026-09-01")))
                .isInstanceOf(InvalidCommuterPassException.class)
                .hasMessage("startDate must be on or before endDate");
    }

    @Test
    void passMemoNormalizesBlankValue() {
        assertThat(new PassMemo(" ").value()).isNull();
    }
}
