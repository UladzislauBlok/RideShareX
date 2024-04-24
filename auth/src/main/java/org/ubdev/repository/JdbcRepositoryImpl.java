package org.ubdev.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.ubdev.jwt.model.Token;

import java.util.Optional;

import static org.ubdev.repository.SqlQueries.*;

@Repository
@RequiredArgsConstructor
public class JdbcRepositoryImpl implements JdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isTokenBanned(Token token) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(IS_TOKEN_BANNED_QUERY, Boolean.class, token.id()));
    }

    @Override
    public Optional<UserDetails> getUserByEmail(String email) {
        return jdbcTemplate.query(GET_USER_BY_EMAIL_QUERY,
                (rs, i) -> User.builder()
                        .username(rs.getString("email"))
                        .password(rs.getString("password"))
                        .authorities(
                                jdbcTemplate.query(GET_USER_AUTHORITY_BY_USER_ID_QUERY,
                                        (rs1, i1) ->
                                                new SimpleGrantedAuthority(rs1.getString("authority")),
                                        email))
                        .build(), email).stream().findFirst();
    }
}
