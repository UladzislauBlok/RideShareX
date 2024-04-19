package org.ubdev.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.ubdev.security.model.Token;

@Repository
@RequiredArgsConstructor
public class JdbcRepositoryImpl implements JdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isTokenBanned(Token token) {
        return false;
    }
}
