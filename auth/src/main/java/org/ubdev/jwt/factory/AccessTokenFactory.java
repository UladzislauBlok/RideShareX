package org.ubdev.jwt.factory;

import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenType;

import java.time.Duration;
import java.time.Instant;

import static org.ubdev.jwt.config.JwtConstants.AccessTokenTtlInMinutes;

public class AccessTokenFactory extends AbstractTokenFactory<Token> {

    public AccessTokenFactory() {
        super(TokenType.ACCESS, Duration.ofMinutes(AccessTokenTtlInMinutes));
    }

    @Override
    public Token createToken(Token token) {
        Instant now = Instant.now();
        return new Token(token.id(), token.subject(),
                token.authorities().stream()
                        .filter(authority -> authority.startsWith("GRANT_"))
                        .map(authority -> authority.substring(6))
                        .toList(), now, now.plus(ttl), tokenType);
    }
}
