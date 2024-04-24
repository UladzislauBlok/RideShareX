package org.ubdev.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ubdev.jwt.factory.TokenFactory;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.model.TokenResponse;
import org.ubdev.jwt.serializer.JwtTokenStringSerializer;

import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;
import static org.ubdev.jwt.config.JwtConstants.ACCESS_DENIED_MESSAGE;

@Builder
@RequiredArgsConstructor
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/jwt/tokens", POST.name());

    private final SecurityContextRepository securityContextRepository;
    private final TokenFactory<Authentication> refreshTokenFactory;
    private final TokenFactory<Token> accessTokenFactory;
    private final JwtTokenStringSerializer refreshTokenSerializer;
    private final JwtTokenStringSerializer accessTokenSerializer;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                    var refreshToken = this.refreshTokenFactory.createToken(context.getAuthentication());
                    var accessToken = this.accessTokenFactory.createToken(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new TokenResponse(this.accessTokenSerializer.serialize(accessToken),
                                    accessToken.expiresAt().toString(),
                                    this.refreshTokenSerializer.serialize(refreshToken),
                                    refreshToken.expiresAt().toString()));
                    return;
                }
            }

            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        filterChain.doFilter(request, response);
    }
}
