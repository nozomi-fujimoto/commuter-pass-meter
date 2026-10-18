package com.example.commuterpassmeter.commuterpass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CommuterPassServiceTest {

    private final CommuterPassService service = new CommuterPassService(
            new InMemoryCommuterPassRepository(Clock.systemUTC()), Clock.systemUTC());

    @ParameterizedTest
    @ValueSource(ints = {1, 1073741823})
    void acceptsSafeOneWayFaresOnCreateAndUpdate(int fare) {
        CommuterPass created = service.create(request(fare));
        assertEquals(fare, created.oneWayFare().value());
        assertEquals(Integer.MAX_VALUE / 2, created.passPrice().value());
        CommuterPass updated = service.update(created.id(), request(fare));
        assertEquals(fare, updated.oneWayFare().value());
        assertEquals(Integer.MAX_VALUE / 2, updated.passPrice().value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1, 0, 1073741824, Integer.MAX_VALUE})
    void rejectsInvalidOneWayFaresWithoutChangingSavedPass(Integer fare) {
        CommuterPass existing = service.create(request(310));
        assertThrows(InvalidCommuterPassException.class, () -> service.create(request(fare)));
        assertEquals(existing, service.getActive());
        assertThrows(InvalidCommuterPassException.class, () -> service.update(existing.id(), request(fare)));
        assertEquals(existing, service.getActive());
    }

    private CommuterPassRequest request(Integer fare) {
        return new CommuterPassRequest("横浜", "品川", fare, Integer.MAX_VALUE / 2,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31), null);
    }
}
