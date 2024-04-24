package org.ubdev.jwt.deserializer;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenType;
import org.ubdev.jwt.util.JwtUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.ubdev.jwt.config.JwtConstants.AUTHORITIES;

@Slf4j
public class RefreshTokenJweStringDeserializer implements JwtDeserializer {
    private final String secret;

    public RefreshTokenJweStringDeserializer(String secret) {
        this.secret = secret;
    }

    @Override
    public Optional<Token> deserialize(String token) {
        try {
            var claims = Jwts.parser()
                    .decryptWith(JwtUtils.getSignKey(secret))
                    .build()                          // (3)
                    .parseEncryptedClaims(token)
                    .getPayload();

            return Optional.of(new Token(
                    UUID.fromString(claims.getId()),
                    claims.getSubject(),
                    claims.get(AUTHORITIES, ArrayList.class),
                    claims.getIssuedAt().toInstant(),
                    claims.getExpiration().toInstant(),
                    TokenType.REFRESH
            ));
        } catch (JwtException e) {
            log.warn(e.getMessage());
        }

        return Optional.empty();
    }
}
