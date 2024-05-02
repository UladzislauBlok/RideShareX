package org.ubdev.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.ubdev.user.repository.UserSqlQueries.GET_USER_AUTHORITY_BY_USER_ID_QUERY;
import static org.ubdev.user.repository.UserSqlQueries.GET_USER_BY_EMAIL_QUERY;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

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
