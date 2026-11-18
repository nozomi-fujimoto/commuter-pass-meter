package com.example.commuterpassmeter.dashboard;

/** 累計運賃と定期料金との差額です。未回収時は負数になります。 */
public record FareDifference(long value) {
    public boolean isRecovered() {
        return value >= 0;
    }
}
