package com.example.commuterpassmeter.health;

import java.time.OffsetDateTime;

public record HealthResponse(String status, OffsetDateTime checkedAt) {
}
