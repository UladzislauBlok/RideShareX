package org.ubdev.jwt.serializer;

import io.jsonwebtoken.Jwts;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.util.JwtUtils;

import java.util.Date;

public class AccessTokenJwsStringSerializer implements JwtTokenStringSerializer {
    private final String secret;

    public AccessTokenJwsStringSerializer(String secret) {
        this.secret = secret;
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
                .signWith(JwtUtils.getSignKey(secret))
                .compact();
    }
}
