package org.ubdev.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.ubdev.security.config.Constants.SYSTEM_HEADER_ERROR_MESSAGE;
import static org.ubdev.security.config.Constants.SYSTEM_MESSAGE_HEADER;
import static org.ubdev.security.config.Constants.X_MESSAGE_HEADER;

@Order(2)
@Component
@RequiredArgsConstructor
public class SystemHeaderFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getHeaders().containsKey(SYSTEM_MESSAGE_HEADER)) {
            return buildAccessDeniedResponse(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> buildAccessDeniedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add(X_MESSAGE_HEADER, SYSTEM_HEADER_ERROR_MESSAGE);
        return Mono.empty();
    }
}
