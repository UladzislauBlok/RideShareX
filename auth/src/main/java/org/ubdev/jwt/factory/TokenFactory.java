package org.ubdev.jwt.factory;

import org.ubdev.jwt.model.Token;

public interface TokenFactory <T> {
    Token createToken(T source);
}
