package org.ubdev.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;

import static org.ubdev.security.config.SecurityConstants.INCORRECT_SUBJECT_AUTHORITIES_TYPE_MESSAGE;

public class HeaderAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {


    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        if (token.getPrincipal() instanceof String subject && token.getCredentials() instanceof String authorityString) {
            var authorities = Arrays.stream(authorityString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new User(subject, "nopassword", authorities);
        }

        throw new UsernameNotFoundException(INCORRECT_SUBJECT_AUTHORITIES_TYPE_MESSAGE);
    }
}
