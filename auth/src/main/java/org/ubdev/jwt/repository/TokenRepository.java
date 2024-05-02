package org.ubdev.jwt.repository;

import org.ubdev.jwt.model.Token;

public interface TokenRepository {
    boolean isTokenBanned(Token token);
    void banToken(Token token);
}
