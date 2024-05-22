package org.ubdev.jwt.serializer;

import org.ubdev.jwt.model.Token;

public interface JwtTokenStringSerializer {
    String serialize(Token token);
}
