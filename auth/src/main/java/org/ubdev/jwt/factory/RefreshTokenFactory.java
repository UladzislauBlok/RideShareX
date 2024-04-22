package org.ubdev.jwt.factory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenType;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.ubdev.jwt.config.JwtConstants.*;

public class RefreshTokenFactory extends AbstractTokenFactory<Authentication> {

    public RefreshTokenFactory() {
        super(TokenType.REFRESH, Duration.ofDays(RefreshTokenTtlInDays));
    }

    @Override
    public Token createToken(Authentication authentication) {
        List<String> authorities = new ArrayList<>();
        authorities.add(JWT_REFRESH);
        authorities.add(JWT_LOGOUT);
        authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> GRANT_PREFIX + authority)
                .forEach(authorities::add);

        Instant now = Instant.now();
        return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(ttl), tokenType);
    }
}
