package org.ubdev.jwt.filter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.model.Token;
import org.ubdev.jwt.repository.TokenRepository;

import java.util.List;

public class JwtLogoutFilter extends BaseJweFilter {

    public JwtLogoutFilter(JwtDeserializer jweDeserializer, TokenRepository tokenRepository, List<String> requiredAuthorities) {
        super(new AntPathRequestMatcher("/api/jwt/logout", HttpMethod.POST.name()), 
                jweDeserializer, tokenRepository, requiredAuthorities);
    }

    @Override
    protected void doFinalFilterOperation(Token token, HttpServletResponse response) {
        tokenRepository.banToken(token);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public static TokenLogoutFilterBuilder builder() {
        return new TokenLogoutFilterBuilder();
    }

    public static class TokenLogoutFilterBuilder {
        private JwtDeserializer jweDeserializer;
        private TokenRepository tokenRepository;
        private List<String> requiredAuthorities;

        TokenLogoutFilterBuilder() {
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder jweDeserializer(final JwtDeserializer jweDeserializer) {
            this.jweDeserializer = jweDeserializer;
            return this;
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder jdbcRepository(final TokenRepository tokenRepository) {
            this.tokenRepository = tokenRepository;
            return this;
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder requiredAuthorities(final List<String> requiredAuthorities) {
            this.requiredAuthorities = requiredAuthorities;
            return this;
        }

        public JwtLogoutFilter build() {
            return new JwtLogoutFilter(this.jweDeserializer, this.tokenRepository, this.requiredAuthorities);
        }
    }
}
