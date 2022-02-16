package dev.ncns.sns.gateway.config.filter;

import dev.ncns.sns.gateway.util.SwaggerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * Gateway와 연결된 Service들의 API 문서를 통합하기 위해 정의하였습니다.
 */
@Slf4j
@Component
public class SwaggerGatewayFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            /**
             * Swagger 요청이 아니면 반환합니다.
             */
            if (!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
                return chain.filter(exchange);
            }

            log.info("[Swagger] - " + request.getMethod() + " " + request.getURI());

            /**
             * 기존 요청을 Swagger 주소로 변환하여 해당 서버의 Swagger 주소로 재요청합니다.
             */
            ServerHttpRequest newRequest = request.mutate().path(SwaggerProvider.API_URI).build();
            ServerWebExchange newWebExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newWebExchange);
        };
    }

}
