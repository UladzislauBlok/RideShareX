package org.ubdev.jwt.factory;

import lombok.RequiredArgsConstructor;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenType;

import java.time.Duration;

@RequiredArgsConstructor
public abstract class AbstractTokenFactory<T> implements TokenFactory<T> {
    protected final TokenType tokenType;
    protected final Duration ttl;

    @Override
    public abstract Token createToken(T source);
}
