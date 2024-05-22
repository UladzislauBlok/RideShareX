package org.ubdev.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.ubdev.user.repository.UserSqlQueries.*;

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

    @Override
    public boolean existsByEmail(String email) {
        return jdbcTemplate.query(GET_USER_BY_EMAIL_QUERY, (rs, i) -> rs.getString("email"), email).stream().findFirst().isPresent();
    }

    @Override
    public void saveUser(org.ubdev.user.model.User user) {
        jdbcTemplate.update(INSERT_USER_QUERY, user.getId(), user.getEmail(), user.getPassword(), user.isEmailConfirmed());
        jdbcTemplate.update(ADD_AUTHORITY_TO_USER_QUERY, user.getId());
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) {
        jdbcTemplate.update(UPDATE_USER_EMAIL_QUERY, newEmail, oldEmail);
    }

    @Override
    public void updatePassword(String email, String password) {
        jdbcTemplate.update(UPDATE_USER_PASSWORD_QUERY, password, email);
    }

    @Override
    public void deleteUser(String email) {
        jdbcTemplate.update(DELETE_USER_BY_EMAIL_QUERY, email);
    }

    @Override
    public void deleteUserById(UUID id) {
        jdbcTemplate.update(DELETE_USER_BY_ID_QUERY, id);
    }
}
