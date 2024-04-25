package org.ubdev.jwt.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.factory.TokenFactory;
import org.ubdev.jwt.filter.JwtLogoutFilter;
import org.ubdev.jwt.filter.RefreshTokenFilter;
import org.ubdev.jwt.filter.RequestJwtTokensFilter;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.serializer.JwtTokenStringSerializer;
import org.ubdev.repository.JdbcRepository;

@Builder
@RequiredArgsConstructor
public class JwtAuthenticationConfigurer
        extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final SecurityContextRepository securityContextRepository;
    private final TokenFactory<Authentication> refreshTokenFactory;
    private final TokenFactory<Token> accessTokenFactory;
    private final JwtTokenStringSerializer refreshTokenSerializer;
    private final JwtTokenStringSerializer accessTokenSerializer;
    private final JwtDeserializer jweDeserializer;
    private final ObjectMapper objectMapper;
    private final JdbcRepository jdbcRepository;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestJwtTokensFilter = buildRequestJwtTokenFilter();
        var refreshJwtTokensFilter = buildRefreshTokenFilter();
        var jwtLogoutFilter = buildJwtLogoutFilter();

        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(refreshJwtTokensFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtLogoutFilter, BasicAuthenticationFilter.class);
    }

    private JwtLogoutFilter buildJwtLogoutFilter() {
        return JwtLogoutFilter.builder()
                .jweDeserializer(jweDeserializer)
                .jdbcRepository(jdbcRepository)
                .build();
    }

    private RefreshTokenFilter buildRefreshTokenFilter() {
        return RefreshTokenFilter.builder()
                .jweDeserializer(jweDeserializer)
                .accessTokenFactory(accessTokenFactory)
                .accessTokenStringSerializer(accessTokenSerializer)
                .objectMapper(objectMapper)
                .jdbcRepository(jdbcRepository)
                .build();
    }

    private RequestJwtTokensFilter buildRequestJwtTokenFilter() {
        return RequestJwtTokensFilter.builder()
                .securityContextRepository(securityContextRepository)
                .refreshTokenFactory(refreshTokenFactory)
                .accessTokenFactory(accessTokenFactory)
                .refreshTokenSerializer(refreshTokenSerializer)
                .accessTokenSerializer(accessTokenSerializer)
                .objectMapper(objectMapper)
                .build();
    }
}
