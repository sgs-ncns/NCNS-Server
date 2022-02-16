package dev.ncns.sns.gateway.config.filter;

import dev.ncns.sns.gateway.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 들어온 Request에 대한 로깅을 위해 정의하였습니다.
 */
@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String curlX = String.format("-X %s %s", request.getMethod(), request.getURI());
        String curlH = String.format("-H %s %s", Constants.AUTH_HEADER_KEY, request.getHeaders().get(Constants.AUTH_HEADER_KEY));

        log.info("[Logging] - " + curlX + " " + curlH);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
