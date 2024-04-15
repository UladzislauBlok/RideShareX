package org.ubdev.jwt.serializer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.ubdev.jwt.model.Token;

import javax.crypto.SecretKey;
import java.util.Date;

public class RefreshTokenJweStringSerializer implements JwtTokenStringSerializer {
    private final String secret;
    @Setter
    private AeadAlgorithm enc = Jwts.ENC.A128CBC_HS256;

    public RefreshTokenJweStringSerializer(@Value("${jwe.secret}") String secret) {
        this.secret = secret;
    }

    public SecretKey getSignKey() {
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
                .encryptWith(getSignKey(), enc)
                .compact();
    }
}
