package com.example.commuterpassmeter.health;

import java.time.OffsetDateTime;

/**
 * ヘルスチェックAPIのレスポンスです。
 */
public record HealthResponse(String status, OffsetDateTime checkedAt) {
}
