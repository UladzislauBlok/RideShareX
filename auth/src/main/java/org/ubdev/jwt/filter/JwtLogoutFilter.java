package org.ubdev.jwt.filter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ubdev.jwt.deserializer.JwtDeserializer;
import org.ubdev.jwt.model.Token;
import org.ubdev.repository.JdbcRepository;

import java.util.List;

public class JwtLogoutFilter extends BaseJweFilter {

    public JwtLogoutFilter(JwtDeserializer jweDeserializer, JdbcRepository jdbcRepository, List<String> requiredAuthorities) {
        super(new AntPathRequestMatcher("/api/jwt/logout", HttpMethod.POST.name()), 
                jweDeserializer, jdbcRepository, requiredAuthorities);
    }

    @Override
    protected void doFinalFilterOperation(Token token, HttpServletResponse response) {
        jdbcRepository.banToken(token);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public static TokenLogoutFilterBuilder builder() {
        return new TokenLogoutFilterBuilder();
    }

    public static class TokenLogoutFilterBuilder {
        private JwtDeserializer jweDeserializer;
        private JdbcRepository jdbcRepository;
        private List<String> requiredAuthorities;

        TokenLogoutFilterBuilder() {
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder jweDeserializer(final JwtDeserializer jweDeserializer) {
            this.jweDeserializer = jweDeserializer;
            return this;
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder jdbcRepository(final JdbcRepository jdbcRepository) {
            this.jdbcRepository = jdbcRepository;
            return this;
        }

        public JwtLogoutFilter.TokenLogoutFilterBuilder requiredAuthorities(final List<String> requiredAuthorities) {
            this.requiredAuthorities = requiredAuthorities;
            return this;
        }

        public JwtLogoutFilter build() {
            return new JwtLogoutFilter(this.jweDeserializer, this.jdbcRepository, this.requiredAuthorities);
        }
    }
}
