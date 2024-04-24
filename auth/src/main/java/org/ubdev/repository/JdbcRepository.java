package org.ubdev.repository;

import org.ubdev.jwt.model.Token;

public interface JdbcRepository {
    boolean isTokenBanned(Token token);
}
