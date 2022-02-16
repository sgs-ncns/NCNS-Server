package dev.ncns.sns.auth.service;

import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.util.JwtProvider;
import dev.ncns.sns.auth.util.RedisManager;
import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisManager redisManager;

    public AuthResponseDto issueToken(LoginResponseDto loginResponse) {
        /**
         * 페이로드에 userId가 담긴 AccessToken과 RefreshToken을 생성합니다.
         */
        String userId = loginResponse.getUserId().toString();
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        /**
         * Redis에 RefreshToken을 저장합니다.
         */
        redisManager.setValue(jwtProvider.getRefreshTokenKey(userId), refreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);

        return AuthResponseDto.of(accessToken, refreshToken);
    }

    public void discardToken(String authorization, String refreshToken) {
        String accessToken = jwtProvider.getAccessToken(authorization);

        /**
         * AccessToken을 검증하고 RefreshToken과 사용자가 일치하는지 체크합니다.
         */
        validateToken(refreshToken);
        compareUserId(accessToken, refreshToken);

        /**
         * Redis에 저장된 RefreshToken을 삭제합니다.
         */
        String userId = getUserId(accessToken);
        redisManager.deleteValue(jwtProvider.getRefreshTokenKey(userId));

        /**
         * 해당 AccessToken을 다시 사용할 수 없도록 Redis에 블랙리스트 처리합니다.
         */
        long timeout = jwtProvider.getExpirationDate(accessToken);
        redisManager.setValue(accessToken, jwtProvider.getBlackListTokenValue(userId), timeout);
    }

    public AuthResponseDto reissueToken(String refreshToken) {
        validateToken(refreshToken);

        /**
         * 사용자의 RefreshToken을 검증 후 Redis에 저장된 RefreshToken과 비교합니다.
         */
        String userId = getUserId(refreshToken);
        String cachedRefreshToken = redisManager.getValue(jwtProvider.getRefreshTokenKey(userId));

        compareToken(refreshToken, cachedRefreshToken);

        String accessToken = jwtProvider.createAccessToken(userId);

        /**
         * AccessToken을 재발급하고, 만약 RefreshToken의 유효기간이 1/3도 남지 않았다면 RefreshToken도 재발급합니다.
         */
        if (jwtProvider.isRefreshTokenExpiration(refreshToken)) {
            refreshToken = jwtProvider.createRefreshToken(userId);
            redisManager.setValue(jwtProvider.getRefreshTokenKey(userId), refreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);
        }
        return AuthResponseDto.of(accessToken, refreshToken);
    }

    public String getUserId(String token) {
        return jwtProvider.getSubject(token);
    }

    private void validateToken(String token) {
        ResponseType responseType = jwtProvider.validateToken(token);
        if (!responseType.equals(ResponseType.SUCCESS)) {
            throw new BadRequestException(responseType);
        }
    }

    private void compareToken(String target, String token) {
        if (!token.equals(target)) {
            throw new BadRequestException(ResponseType.AUTH_NOT_SAME_TOKEN);
        }
    }

    private void compareUserId(String accessToken, String refreshToken) {
        if (!getUserId(accessToken).equals(getUserId(refreshToken))) {
            throw new BadRequestException(ResponseType.AUTH_NOT_SAME_USER);
        }
    }

}
