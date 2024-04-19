package org.ubdev.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ubdev.security.deserializer.JwtDeserializer;
import org.ubdev.security.model.Token;
import org.ubdev.security.path.PathConstants;
import org.ubdev.security.repository.JdbcRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class JwtDeserializationFilter implements GlobalFilter {
    private final JwtDeserializer jwtDeserializer;
    private final JdbcRepository jdbcRepository;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var requestPath = exchange.getRequest().getPath().value();
        log.info("requestPath: {}", requestPath);

        if (PathConstants.PUBLIC_PATH.contains(requestPath))
            return chain.filter(exchange);

        if (PathConstants.REFRESH_TOKEN_PATH.contains(requestPath))
            return chain.filter(exchange);

        var requestHeaders = exchange.getRequest().getHeaders();
        var authHeaders = requestHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            return buildUnauthorizedResponse(exchange);
        log.info(authHeaders);
        if (!authHeaders.startsWith("Bearer "))
            return buildUnauthorizedResponse(exchange);

        var jwtTokenString = authHeaders.substring(7);
        log.info(jwtTokenString);
        var authToken = jwtDeserializer.deserialize(jwtTokenString);
        if (authToken.isEmpty())
            return buildUnauthorizedResponse(exchange);
        if (jdbcRepository.isTokenBanned(authToken.get()))
            return buildUnauthorizedResponse(exchange);

        exchange = addAuthHeaders(exchange, authToken.get());
        return chain.filter(exchange);
    }

    private ServerWebExchange addAuthHeaders(ServerWebExchange exchange, Token token) {
        return exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .headers(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION))
                                .header("Subject", token.subject())
                                .header("Authorities", String.join(",", token.authorities()))
                                .build())
                .build();
    }

    private Mono<Void> buildUnauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Message", "Invalid token");
        return Mono.empty();
    }
}
