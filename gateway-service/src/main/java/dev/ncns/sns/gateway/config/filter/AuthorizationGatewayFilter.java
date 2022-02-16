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

/**
 * Request에 담긴 AccessToken을 검증하고 파싱하기 위해 정의하였습니다.
 */
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

            /**
             * Swagger 문서 요청은 pass 하도록 합니다.
             */
            if (passSwagger(request.getURI().getPath())) {
                return chain.filter(exchange);
            }

            /**
             * Authorization Header에 담긴 AccessToken을 검증합니다.
             */
            String accessToken = jwtProvider.getAccessToken(getAuthorization(request.getHeaders()));
            ResponseType responseType = jwtProvider.validateToken(accessToken);
            if (!responseType.equals(ResponseType.SUCCESS)) {
                throw new BadRequestException(responseType);
            }

            /**
             * 만약 AccessToken이 블랙리스트 처리가 된 토큰이라면 이용하지 못하도록 합니다.
             */
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
