package org.ubdev.repository;

import org.springframework.stereotype.Component;
import org.ubdev.jwt.model.Token;

@Component
public class JdbcRepositoryImpl implements JdbcRepository{
    @Override
    public boolean isTokenBanned(Token token) {
        return false;
    }
}
