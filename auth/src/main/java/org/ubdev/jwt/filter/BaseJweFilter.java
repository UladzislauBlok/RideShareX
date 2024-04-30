package org.ubdev.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.exception.*;
import org.ubdev.jwt.model.Token;
import org.ubdev.repository.JdbcRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.ubdev.jwt.config.JwtConstants.*;

@RequiredArgsConstructor
public abstract class BaseJweFilter extends OncePerRequestFilter {
    protected final RequestMatcher requestMatcher;
    protected final JwtDeserializer jweDeserializer;
    protected final JdbcRepository jdbcRepository;
    protected final List<String> requiredAuthorities;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {

                authHeader = extractBearerHeader(authHeader);
                var token = deserializeJweFromString(authHeader);
                checkIsUserHasAuthorities(token, requiredAuthorities);
                checkTokenExpiration(token);
                checkIsTokenBanned(token);

                doFinalFilterOperation(token, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    protected abstract void doFinalFilterOperation(Token token, HttpServletResponse response) throws ServletException, IOException;

    protected void checkIsTokenBanned(Token token) {
        if (jdbcRepository.isTokenBanned(token))
            throw new BannedTokenException(BANNED_TOKEN_EXCEPTION_MESSAGE);
    }

    protected void checkTokenExpiration(Token token) {
        if (token.expiresAt().isBefore(Instant.now()))
            throw new TokenExpiredException(TOKEN_EXPIRED_EXCEPTION_MESSAGE);
    }

    protected void checkIsUserHasAuthorities(Token token, List<String> authorities) {
        if (!new HashSet<>(token.authorities()).containsAll(authorities))
            throw new UnauthorizedAuthoritiesException(INCORRECT_JWT_TOKEN_EXCEPTION_MESSAGE);
    }

    protected Token deserializeJweFromString(String authHeader) {
        return jweDeserializer.deserialize(authHeader)
                .orElseThrow(() -> new JweDeserializationException(JWE_DESERIALIZATION_EXCEPTION_MESSAGE));
    }

    protected String extractBearerHeader(String authHeader) {
        if (!authHeader.startsWith(BEARER_PREFIX))
            throw new IncorrectTokenTypeException(INCORRECT_TOKEN_TYPE_EXCEPTION);
        authHeader = authHeader.substring(BEARER_PREFIX.length());
        return authHeader;
    }
}
