package org.ubdev.jwt.serializer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.ubdev.jwt.model.Token;

import java.security.Key;
import java.util.Date;

public class AccessTokenJwsStringSerializer implements JwtTokenStringSerializer {
    private final String secret;

    public AccessTokenJwsStringSerializer(@Value("${jws.secret}") String secret) {
        this.secret = secret;
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String serialize(Token token) {
        return Jwts.builder()
                .header()
                .keyId(token.id().toString())
                .and()
                .claims()
                .id(token.id().toString())
                .subject(token.subject())
                .issuedAt(Date.from(token.createdAt()))
                .expiration(Date.from(token.expiresAt()))
                .add("authorities", token.authorities())
                .and()
                .signWith(getSignKey())
                .compact();
    }
}
