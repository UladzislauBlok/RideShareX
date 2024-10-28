package org.ubdev.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers("/api/v1/trip").permitAll()
                                .requestMatchers("/api/v1/email/{email}").permitAll()
                                .requestMatchers("/api/v1/users/all").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasRole("USER")
                                .requestMatchers("/api/v1/users").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/ratings/{id}").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/ratings/{id}").hasRole("USER")
                                .requestMatchers("/api/v1/ratings").hasRole("USER")
                                .requestMatchers("/api/v1/documents/{id}").hasRole("MANAGER")
                                .requestMatchers("/api/v1/documents/user/{id}").hasRole("MANAGER")
                                .requestMatchers("/api/v1/documents").hasRole("USER")
                                .requestMatchers("/api/v1/cars/user/{id}").hasRole("MANAGER")
                                .requestMatchers("/api/v1/cars/{id}").hasRole("MANAGER")
                                .requestMatchers("/api/v1/cars").hasRole("USER")
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
