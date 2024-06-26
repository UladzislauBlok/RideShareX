package org.ubdev.security.configurer;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.ubdev.security.converter.HeaderAuthenticationConverter;
import org.ubdev.security.service.HeaderAuthenticationUserDetailsService;

@Builder
@RequiredArgsConstructor
public class HeaderAuthenticationConfigurer
        extends AbstractHttpConfigurer<HeaderAuthenticationConfigurer, HttpSecurity> {

    public void configure(HttpSecurity builder) throws Exception {
        var headerAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new HeaderAuthenticationConverter());
        headerAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {});
        headerAuthenticationFilter.setFailureHandler(((request, response, exception) ->{}));

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new HeaderAuthenticationUserDetailsService());

        builder.addFilterBefore(headerAuthenticationFilter, BasicAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}