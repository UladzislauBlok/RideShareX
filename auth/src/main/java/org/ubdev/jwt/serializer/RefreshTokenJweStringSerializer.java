package org.ubdev.jwt.serializer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import lombok.Setter;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.util.JwtUtils;

import java.util.Date;

import static org.ubdev.jwt.config.JwtConstants.AUTHORITIES;

public class RefreshTokenJweStringSerializer implements JwtTokenStringSerializer {
    private final String secret;
    @Setter
    private AeadAlgorithm enc = Jwts.ENC.A128CBC_HS256;

    public RefreshTokenJweStringSerializer(String secret) {
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
                .add(AUTHORITIES, token.authorities())
                .and()
                .encryptWith(JwtUtils.getSignKey(secret), enc)
                .compact();
    }
}
