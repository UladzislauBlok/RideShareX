package org.blokdev.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.blokdev.security.configurer.HeaderAuthenticationConfigurer;
import org.blokdev.security.response.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

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
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers("/api/v1/ride/cities").permitAll()
                                .requestMatchers("/api/v1/ride/countries").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/ride/{id}").hasRole("MANAGER")
                                .requestMatchers("/api/v1/ride/{tripId}/join-requests").hasRole("USER")
                                .requestMatchers("/api/v1/ride/join-requests/decision").hasRole("USER")
                                .requestMatchers("/api/v1/ride/{tripId}/start").hasRole("USER")
                                .requestMatchers("/api/v1/ride/{tripId}/end").hasRole("USER")
                                .requestMatchers("/api/v1/ride/{tripId}/end").hasRole("USER")
                                .requestMatchers("/api/v1/ride/ratings").hasRole("USER")
                                .requestMatchers("/api/v1/ride").hasRole("USER")
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
