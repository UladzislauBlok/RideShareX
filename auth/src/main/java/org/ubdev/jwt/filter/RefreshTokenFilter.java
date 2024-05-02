package org.ubdev.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.factory.TokenFactory;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenResponse;
import org.ubdev.jwt.serializer.JwtTokenStringSerializer;
import org.ubdev.jwt.repository.TokenRepository;

import java.io.IOException;
import java.util.List;

public class RefreshTokenFilter extends BaseJweFilter {
    private final TokenFactory<Token> accessTokenFactory;
    private final JwtTokenStringSerializer accessTokenStringSerializer;
    private final ObjectMapper objectMapper;

    public RefreshTokenFilter(JwtDeserializer jweDeserializer, TokenRepository tokenRepository,
                              List<String> requiredAuthorities, TokenFactory<Token> accessTokenFactory,
                              JwtTokenStringSerializer accessTokenStringSerializer, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/jwt/refresh", HttpMethod.POST.name())
                ,jweDeserializer, tokenRepository, requiredAuthorities);
        
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenStringSerializer = accessTokenStringSerializer;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFinalFilterOperation(Token token, HttpServletResponse response) throws ServletException, IOException {
        var accessToken = this.accessTokenFactory.createToken(token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        this.objectMapper.writeValue(response.getWriter(),
                new TokenResponse(this.accessTokenStringSerializer.serialize(accessToken),
                        accessToken.expiresAt().toString(), null, null));
    }

    public static RefreshTokenFilter.RefreshTokenFilterBuilder builder() {
        return new RefreshTokenFilterBuilder();
    }

    public static class RefreshTokenFilterBuilder {
        private JwtDeserializer jweDeserializer;
        private TokenRepository tokenRepository;
        private List<String> requiredAuthorities;
        private TokenFactory<Token> accessTokenFactory;
        private JwtTokenStringSerializer accessTokenStringSerializer;
        private ObjectMapper objectMapper;

        RefreshTokenFilterBuilder() {
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder jweDeserializer(final JwtDeserializer jweDeserializer) {
            this.jweDeserializer = jweDeserializer;
            return this;
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder tokenRepository(final TokenRepository tokenRepository) {
            this.tokenRepository = tokenRepository;
            return this;
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder requiredAuthorities(final List<String> requiredAuthorities) {
            this.requiredAuthorities = requiredAuthorities;
            return this;
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder accessTokenFactory(final TokenFactory<Token> accessTokenFactory) {
            this.accessTokenFactory = accessTokenFactory;
            return this;
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder accessTokenStringSerializer(final JwtTokenStringSerializer accessTokenStringSerializer) {
            this.accessTokenStringSerializer = accessTokenStringSerializer;
            return this;
        }

        public RefreshTokenFilter.RefreshTokenFilterBuilder objectMapper(final ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public RefreshTokenFilter build() {
            return new RefreshTokenFilter(this.jweDeserializer, this.tokenRepository, this.requiredAuthorities,
                    this.accessTokenFactory, this.accessTokenStringSerializer, this.objectMapper);
        }
    }
}
