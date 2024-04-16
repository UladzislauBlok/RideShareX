package org.ubdev.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.ubdev.jwt.configurer.JwtAuthenticationConfigurer;
import org.ubdev.jwt.factory.AccessTokenFactory;
import org.ubdev.jwt.factory.RefreshTokenFactory;
import org.ubdev.jwt.serializer.AccessTokenJwsStringSerializer;
import org.ubdev.jwt.serializer.RefreshTokenJweStringSerializer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(ObjectMapper objectMapper,
                                                                   JdbcTemplate jdbcTemplate,
                                                                   @Value("${jwt.secret.jwe}") String secretStringJwe,
                                                                   @Value("${jwt.secret.jws}") String secretStringJws) {
        return new JwtAuthenticationConfigurer(securityContextRepository(),
                new RefreshTokenFactory(),
                new AccessTokenFactory(),
                new RefreshTokenJweStringSerializer(secretStringJwe),
                new AccessTokenJwsStringSerializer(secretStringJws),
                objectMapper,
                jdbcTemplate);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {
        http.with(jwtAuthenticationConfigurer, Customizer.withDefaults());

        return http
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers("/actuator/health").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }
}
