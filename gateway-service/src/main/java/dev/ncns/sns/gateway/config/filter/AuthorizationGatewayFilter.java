package dev.ncns.sns.gateway.config.filter;

import dev.ncns.sns.common.util.Constants;
import dev.ncns.sns.gateway.util.JwtProvider;
import dev.ncns.sns.gateway.util.SwaggerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@ComponentScan(basePackages = "dev.ncns.sns.common.util")
@Component
public class AuthorizationGatewayFilter extends AbstractGatewayFilterFactory {

    private final JwtProvider jwtProvider;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (passSwagger(request.getURI().getPath())) {
                return chain.filter(exchange);
            }

            String accessToken = jwtProvider.getAccessToken(getAuthorization(request.getHeaders()));
            if (!jwtProvider.validateToken(accessToken)) {
                throw new IllegalArgumentException("Please request reissue token");
            }
            String userId = jwtProvider.getSubject(accessToken);
            log.info("[Authorization] - " + "UserId is " + userId);

            ServerHttpRequest newRequest = request.mutate().header(Constants.USER_HEADER_KEY, userId).build();
            ServerWebExchange newWebExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newWebExchange);
        };
    }

    private boolean passSwagger(String path) {
        if (StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
            log.info("[Authorization] - " + "The swagger request is passed.");
            return true;
        }
        return false;
    }

    private String getAuthorization(HttpHeaders httpHeaders) {
        List<String> authorization = httpHeaders.get(Constants.AUTH_HEADER_KEY);
        if (authorization == null) {
            throw new IllegalArgumentException("Not found accessToken");
        }
        return authorization.get(0);
    }

}