package org.ubdev.security.repository;

import org.ubdev.security.model.Token;

public interface JdbcRepository {
    boolean isTokenBanned(Token token);
}
