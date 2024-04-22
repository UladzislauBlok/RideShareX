package org.ubdev.jwt.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.factory.TokenFactory;
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
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/api/jwt/tokens", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestJwtTokensFilter = RequestJwtTokensFilter.builder()
                .securityContextRepository(securityContextRepository)
                .refreshTokenFactory(refreshTokenFactory)
                .accessTokenFactory(accessTokenFactory)
                .refreshTokenSerializer(refreshTokenSerializer)
                .accessTokenSerializer(accessTokenSerializer)
                .objectMapper(objectMapper)
                .build();
        var refreshJwtTokensFilter = RefreshTokenFilter.builder()
                .jweDeserializer(jweDeserializer)
                .accessTokenFactory(accessTokenFactory)
                .accessTokenStringSerializer(accessTokenSerializer)
                .objectMapper(objectMapper)
                .jdbcRepository(jdbcRepository)
                .build();

        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(refreshJwtTokensFilter, CsrfFilter.class);
    }
}
