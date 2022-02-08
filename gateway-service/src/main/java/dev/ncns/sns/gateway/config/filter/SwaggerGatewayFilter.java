package dev.ncns.sns.gateway.config.filter;

import dev.ncns.sns.gateway.util.SwaggerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class SwaggerGatewayFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            if (!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
                return chain.filter(exchange);
            }

            log.info("[Swagger] - " + request.getMethod() + " " + request.getURI());

            ServerHttpRequest newRequest = request.mutate().path(SwaggerProvider.API_URI).build();
            ServerWebExchange newWebExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newWebExchange);
        };
    }

}
