package org.ubdev.security.deserializer;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ubdev.security.model.Token;
import org.ubdev.security.model.TokenType;
import org.ubdev.security.util.JwtUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class AccessTokenJwsStringDeserializer implements JwtDeserializer {
    private final String secret;

    public AccessTokenJwsStringDeserializer(
            @Value("${jwt.secret.jws}") String secret) {
        this.secret = secret;
    }

    @Override
    public Optional<Token> deserialize(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(JwtUtils.getSignKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(new Token(
                    UUID.fromString(claims.getId()),
                    claims.getSubject(),
                    claims.get("authorities", ArrayList.class),
                    claims.getIssuedAt().toInstant(),
                    claims.getExpiration().toInstant(),
                    TokenType.ACCESS
            ));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return Optional.empty();
    }
}
