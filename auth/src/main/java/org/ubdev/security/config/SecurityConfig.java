package org.ubdev.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.ubdev.jwt.configurer.JwtAuthenticationConfigurer;
import org.ubdev.jwt.deserializer.RefreshTokenJweStringDeserializer;
import org.ubdev.jwt.factory.AccessTokenFactory;
import org.ubdev.jwt.factory.RefreshTokenFactory;
import org.ubdev.jwt.serializer.AccessTokenJwsStringSerializer;
import org.ubdev.jwt.serializer.RefreshTokenJweStringSerializer;
import org.ubdev.jwt.repository.TokenRepository;
import org.ubdev.security.configurer.HeaderAuthenticationConfigurer;
import org.ubdev.security.response.ErrorResponse;
import org.ubdev.security.service.JdbcUserDetailsServiceImpl;
import org.ubdev.user.repository.UserRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(ObjectMapper objectMapper,
                                                                   TokenRepository tokenRepository,
                                                                   @Value("${jwt.secret.jwe}") String secretStringJwe,
                                                                   @Value("${jwt.secret.jws}") String secretStringJws) {
        return new JwtAuthenticationConfigurer(securityContextRepository(),
                new RefreshTokenFactory(),
                new AccessTokenFactory(),
                new RefreshTokenJweStringSerializer(secretStringJwe),
                new AccessTokenJwsStringSerializer(secretStringJws),
                new RefreshTokenJweStringDeserializer(secretStringJwe),
                objectMapper,
                tokenRepository);
    }

    @Bean
    public HeaderAuthenticationConfigurer headerAuthenticationConfigurer() {
        return new HeaderAuthenticationConfigurer();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
                                                   HeaderAuthenticationConfigurer headerAuthenticationConfigurer,
                                                   AccessDeniedHandler accessDeniedHandler,
                                                   AuthenticationEntryPoint entryPoint) throws Exception {
        http.with(jwtAuthenticationConfigurer, Customizer.withDefaults());
        http.with(headerAuthenticationConfigurer, Customizer.withDefaults());

        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling((ex) -> ex.accessDeniedHandler(accessDeniedHandler))
                .exceptionHandling((ex) -> ex.authenticationEntryPoint(entryPoint))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new JdbcUserDetailsServiceImpl(userRepository));
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return ((request, response, accessDeniedException) -> {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            var errorResponse = new ErrorResponse(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN, request.getServletPath());
            String responseString = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(responseString);
        });
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return ((request, response, accessDeniedException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            var errorResponse = new ErrorResponse(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN, request.getServletPath());
            String responseString = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(responseString);
        });
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
