package org.ubdev.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.exception.BannedTokenException;
import org.ubdev.jwt.exception.IncorrectJwtTokenException;
import org.ubdev.jwt.exception.JweDeserializationException;
import org.ubdev.jwt.factory.TokenFactory;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenResponse;
import org.ubdev.jwt.serializer.JwtTokenStringSerializer;
import org.ubdev.repository.JdbcRepository;

import java.io.IOException;

import static org.ubdev.jwt.config.JwtConstants.*;

@Builder
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/refresh", HttpMethod.POST.name());
    private final JwtDeserializer jweDeserializer;
    private final TokenFactory<Token> accessTokenFactory;
    private final JwtTokenStringSerializer accessTokenStringSerializer;
    private final ObjectMapper objectMapper;
    private final JdbcRepository jdbcRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                var optionalRefreshToken = jweDeserializer.deserialize(authHeader);
                if (optionalRefreshToken.isEmpty())
                    throw new JweDeserializationException(JWE_DESERIALIZATION_EXCEPTION_MESSAGE);

                var token = optionalRefreshToken.get();
                if (!token.authorities().contains(JWT_REFRESH))
                    throw new IncorrectJwtTokenException(INCORRECT_JWT_TOKEN_EXCEPTION_MESSAGE);

                if (jdbcRepository.isTokenBanned(token))
                    throw new BannedTokenException(BANNED_TOKEN_EXCEPTION_MESSAGE);

                var accessToken = this.accessTokenFactory.createToken(token);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                this.objectMapper.writeValue(response.getWriter(),
                        new TokenResponse(this.accessTokenStringSerializer.serialize(accessToken),
                                accessToken.expiresAt().toString(), null, null));

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
