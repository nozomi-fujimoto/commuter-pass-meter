package com.example.commuterpassmeter.commuterpass;

import java.util.Optional;

public interface CommuterPassRepository {
    Optional<CommuterPass> findActiveByUserId(Long userId);

    Optional<CommuterPass> findByIdAndUserId(Long id, Long userId);

    CommuterPass save(CommuterPass commuterPass);
}
