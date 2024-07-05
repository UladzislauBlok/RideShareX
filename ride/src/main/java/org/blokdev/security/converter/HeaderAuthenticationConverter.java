package org.blokdev.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.blokdev.security.config.SecurityConstants.AUTHORITIES;
import static org.blokdev.security.config.SecurityConstants.SUBJECT;


public class HeaderAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        var subjectHeader = request.getHeader(SUBJECT);
        var authoritiesHeader = request.getHeader(AUTHORITIES);

        if (subjectHeader != null && authoritiesHeader != null) {
            return new PreAuthenticatedAuthenticationToken(subjectHeader, authoritiesHeader);
        }
        return null;
    }
}
