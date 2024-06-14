package org.ubdev.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.ubdev.security.configurer.HeaderAuthenticationConfigurer;
import org.ubdev.security.response.ErrorResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public HeaderAuthenticationConfigurer headerAuthenticationConfigurer() {
        return new HeaderAuthenticationConfigurer();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HeaderAuthenticationConfigurer headerAuthenticationConfigurer,
                                                   AccessDeniedHandler accessDeniedHandler,
                                                   AuthenticationEntryPoint entryPoint) throws Exception {
        http.with(headerAuthenticationConfigurer, Customizer.withDefaults());

        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .anyRequest().authenticated())
                .exceptionHandling((ex) -> ex.accessDeniedHandler(accessDeniedHandler))
                .exceptionHandling((ex) -> ex.authenticationEntryPoint(entryPoint))
                .build();
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
}
