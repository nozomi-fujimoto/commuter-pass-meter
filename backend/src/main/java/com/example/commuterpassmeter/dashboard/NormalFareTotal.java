package com.example.commuterpassmeter.dashboard;

import com.example.commuterpassmeter.attendance.RoundTripFare;
import com.example.commuterpassmeter.commuterpass.FareAmount;

/** 定期を使わず通勤した場合の、0円以上の累計運賃です。 */
public record NormalFareTotal(long value) {
    public NormalFareTotal {
        if (value < 0) {
            throw new IllegalArgumentException("normalFareTotal must be at least 0");
        }
    }

    public static NormalFareTotal calculate(RoundTripFare fare, AttendanceCount count) {
        return new NormalFareTotal(fare.value() * (long) count.value());
    }

    public FareDifference differenceFrom(FareAmount passPrice) {
        return new FareDifference(value - passPrice.value());
    }
}
