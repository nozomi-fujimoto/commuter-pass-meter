package com.example.commuterpassmeter.commuterpass;

import java.util.Optional;

/**
 * 定期券情報の永続化と検索を抽象化するRepositoryです。
 */
public interface CommuterPassRepository {
    Optional<CommuterPass> findActiveByUserId(Long userId);

    Optional<CommuterPass> findByIdAndUserId(Long id, Long userId);

    CommuterPass save(CommuterPass commuterPass);
}
