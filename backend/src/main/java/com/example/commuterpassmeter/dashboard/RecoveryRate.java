package com.example.commuterpassmeter.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.example.commuterpassmeter.commuterpass.FareAmount;

/** 0%以上の回収率。100%超を許可し、小数第1位に四捨五入します。 */
public record RecoveryRate(BigDecimal value) {
    public RecoveryRate {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("recoveryRate must be at least 0");
        }
        value = value.setScale(1, RoundingMode.HALF_UP);
    }

    public static RecoveryRate calculate(NormalFareTotal total, FareAmount passPrice) {
        return new RecoveryRate(BigDecimal.valueOf(total.value()).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(passPrice.value()), 1, RoundingMode.HALF_UP));
    }
}
