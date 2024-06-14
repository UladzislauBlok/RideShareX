package org.ubdev.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.ubdev.jwt.model.Token;

import java.util.Date;

import static org.ubdev.jwt.repository.TokenSqlQueries.BAN_TOKEN_QUERY;
import static org.ubdev.jwt.repository.TokenSqlQueries.IS_TOKEN_BANNED_QUERY;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isTokenBanned(Token token) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(IS_TOKEN_BANNED_QUERY, Boolean.class, token.id()));
    }

    @Override
    public void banToken(Token token) {
        jdbcTemplate.update(BAN_TOKEN_QUERY, token.id(), Date.from(token.expiresAt()));
    }
}
