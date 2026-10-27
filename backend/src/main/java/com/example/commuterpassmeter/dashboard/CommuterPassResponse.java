package com.example.commuterpassmeter.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.commuterpassmeter.commuterpass.CommuterPass;

/** Dashboardに公開する定期情報。ドメインモデルの変更からAPI契約を分離します。 */
public record CommuterPassResponse(Long id, Long userId, String fromStation, String toStation,
        int oneWayFare, int passPrice, LocalDate startDate, LocalDate endDate, String memo,
        Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static CommuterPassResponse from(CommuterPass pass) {
        return new CommuterPassResponse(pass.id(), pass.userId(), pass.fromStation().value(),
                pass.toStation().value(), pass.oneWayFare().value(), pass.passPrice().value(),
                pass.startDate(), pass.endDate(), pass.memo().value(), pass.active(),
                pass.createdAt(), pass.updatedAt());
    }
}
