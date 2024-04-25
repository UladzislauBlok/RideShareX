package org.ubdev.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.ubdev.jwt.model.Token;

import java.util.Optional;

public interface JdbcRepository {
    boolean isTokenBanned(Token token);
    Optional<UserDetails> getUserByEmail(String email);
    void banToken(Token token);
}
