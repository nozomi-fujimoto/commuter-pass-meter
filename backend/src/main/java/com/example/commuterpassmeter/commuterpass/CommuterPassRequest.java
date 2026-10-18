package com.example.commuterpassmeter.commuterpass;

import java.time.LocalDate;

/**
 * 定期券情報の作成・更新APIのリクエストです。
 */
public record CommuterPassRequest(
        String fromStation,
        String toStation,
        Integer oneWayFare,
        Integer passPrice,
        LocalDate startDate,
        LocalDate endDate,
        String memo) {
}
