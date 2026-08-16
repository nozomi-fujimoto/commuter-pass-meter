package com.example.commuterpassmeter.commuterpass;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

class InMemoryCommuterPassRepositoryTest {

    @Test
    void savingNewActivePassUpdatesDeactivatedPassTimestamp() {
        Clock clock = Clock.fixed(Instant.parse("2026-08-17T00:00:00Z"), ZoneId.of("UTC"));
        InMemoryCommuterPassRepository repository = new InMemoryCommuterPassRepository(clock);
        LocalDateTime originalTime = LocalDateTime.parse("2026-08-01T09:00:00");

        CommuterPass first = repository.save(pass(null, "横浜", originalTime));
        repository.save(pass(null, "川崎", LocalDateTime.parse("2026-08-17T09:00:00")));

        CommuterPass deactivated = repository.findByIdAndUserId(first.id(), first.userId()).orElseThrow();

        assertThat(deactivated.active()).isFalse();
        assertThat(deactivated.updatedAt()).isEqualTo(LocalDateTime.parse("2026-08-17T00:00:00"));
        assertThat(deactivated.updatedAt()).isAfter(originalTime);
    }

    private CommuterPass pass(Long id, String fromStation, LocalDateTime timestamp) {
        return new CommuterPass(
                id,
                1L,
                new StationName(fromStation),
                new StationName("品川"),
                new FareAmount(310),
                new FareAmount(14200),
                new PassPeriod(LocalDate.parse("2026-08-01"), LocalDate.parse("2026-08-31")),
                new PassMemo(null),
                true,
                timestamp,
                timestamp);
    }
}
