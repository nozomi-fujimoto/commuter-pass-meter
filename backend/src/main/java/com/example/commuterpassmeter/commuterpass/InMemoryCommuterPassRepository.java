package com.example.commuterpassmeter.commuterpass;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
class InMemoryCommuterPassRepository implements CommuterPassRepository {

    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, CommuterPass> passes = new LinkedHashMap<>();
    private final Clock clock;

    InMemoryCommuterPassRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public synchronized Optional<CommuterPass> findActiveByUserId(Long userId) {
        return passes.values().stream()
                .filter(pass -> pass.userId().equals(userId))
                .filter(CommuterPass::active)
                .findFirst();
    }

    @Override
    public synchronized Optional<CommuterPass> findByIdAndUserId(Long id, Long userId) {
        return Optional.ofNullable(passes.get(id))
                .filter(pass -> pass.userId().equals(userId));
    }

    @Override
    public synchronized CommuterPass save(CommuterPass commuterPass) {
        Long id = commuterPass.id() == null ? sequence.getAndIncrement() : commuterPass.id();
        if (commuterPass.active()) {
            deactivateOtherActivePasses(id, commuterPass.userId());
        }
        CommuterPass saved = new CommuterPass(
                id,
                commuterPass.userId(),
                commuterPass.fromStation(),
                commuterPass.toStation(),
                commuterPass.oneWayFare(),
                commuterPass.passPrice(),
                commuterPass.period(),
                commuterPass.memo(),
                commuterPass.active(),
                commuterPass.createdAt(),
                commuterPass.updatedAt());
        passes.put(id, saved);
        return saved;
    }

    private void deactivateOtherActivePasses(Long activePassId, Long userId) {
        passes.replaceAll((id, pass) -> {
            if (id.equals(activePassId) || !pass.userId().equals(userId) || !pass.active()) {
                return pass;
            }
            return new CommuterPass(
                    pass.id(),
                    pass.userId(),
                    pass.fromStation(),
                    pass.toStation(),
                    pass.oneWayFare(),
                    pass.passPrice(),
                    pass.period(),
                    pass.memo(),
                    false,
                    pass.createdAt(),
                    LocalDateTime.now(clock));
        });
    }
}
