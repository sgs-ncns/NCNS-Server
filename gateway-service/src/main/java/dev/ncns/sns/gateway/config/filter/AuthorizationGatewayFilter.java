package dev.ncns.sns.gateway.config.filter;

import dev.ncns.sns.gateway.domain.ResponseType;
import dev.ncns.sns.gateway.exception.BadRequestException;
import dev.ncns.sns.gateway.exception.UnauthorizedException;
import dev.ncns.sns.gateway.util.Constants;
import dev.ncns.sns.gateway.util.JwtProvider;
import dev.ncns.sns.gateway.util.RedisManager;
import dev.ncns.sns.gateway.util.SwaggerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthorizationGatewayFilter extends AbstractGatewayFilterFactory {

    private final JwtProvider jwtProvider;
    private final RedisManager redisManager;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (passSwagger(request.getURI().getPath())) {
                return chain.filter(exchange);
            }

            String accessToken = jwtProvider.getAccessToken(getAuthorization(request.getHeaders()));
            ResponseType responseType = jwtProvider.validateToken(accessToken);
            if (!responseType.equals(ResponseType.SUCCESS)) {
                throw new BadRequestException(responseType);
            }

            String userId = jwtProvider.getSubject(accessToken);
            log.info("[Authorization] - " + "UserId is " + userId);
            checkBlackListToken(userId, accessToken);

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
            throw new UnauthorizedException(ResponseType.REQUEST_UNAUTHORIZED);
        }
        return authorization.get(0);
    }

    private void checkBlackListToken(String userId, String accessToken) {
        String blackListToken = redisManager.getBlackListValue(jwtProvider.getBlackListTokenValue(userId));
        if (accessToken.equals(blackListToken)) {
            log.info("[Authorization] - " + "This token is already logout.");
            throw new BadRequestException(ResponseType.GATEWAY_BLACK_LIST_TOKEN);
        }
    }

}
