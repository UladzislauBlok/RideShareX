package org.ubdev.jwt.deserializer;

import org.ubdev.jwt.model.Token;

import java.util.Optional;

public interface JwtDeserializer {

    Optional<Token> deserialize(String token);
}
