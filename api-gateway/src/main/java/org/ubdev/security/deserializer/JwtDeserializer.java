package org.ubdev.security.deserializer;

import org.ubdev.security.model.Token;

import java.util.Optional;

public interface JwtDeserializer {

    Optional<Token> deserialize(String token);
}
