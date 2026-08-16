package com.example.commuterpassmeter.commuterpass;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class CommuterPassService {

    static final long FIXED_USER_ID = 1L;

    private final CommuterPassRepository repository;
    private final Clock clock;

    public CommuterPassService(CommuterPassRepository repository) {
        this.repository = repository;
        this.clock = Clock.systemDefaultZone();
    }

    public CommuterPass getActive() {
        return repository.findActiveByUserId(FIXED_USER_ID)
                .orElseThrow(() -> new CommuterPassNotFoundException("Active commuter pass was not found"));
    }

    public CommuterPass create(CommuterPassRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        return repository.save(new CommuterPass(
                null,
                FIXED_USER_ID,
                new StationName(request.fromStation()),
                new StationName(request.toStation()),
                new FareAmount(request.oneWayFare()),
                new FareAmount(request.passPrice()),
                new PassPeriod(request.startDate(), request.endDate()),
                new PassMemo(request.memo()),
                true,
                now,
                now));
    }

    public CommuterPass update(Long id, CommuterPassRequest request) {
        CommuterPass existing = repository.findByIdAndUserId(id, FIXED_USER_ID)
                .orElseThrow(() -> new CommuterPassNotFoundException("Commuter pass was not found"));
        return repository.save(new CommuterPass(
                existing.id(),
                existing.userId(),
                new StationName(request.fromStation()),
                new StationName(request.toStation()),
                new FareAmount(request.oneWayFare()),
                new FareAmount(request.passPrice()),
                new PassPeriod(request.startDate(), request.endDate()),
                new PassMemo(request.memo()),
                true,
                existing.createdAt(),
                LocalDateTime.now(clock)));
    }
}
