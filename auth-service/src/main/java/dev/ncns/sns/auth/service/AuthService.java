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
        String userId = loginResponse.getUserId().toString();
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        redisManager.setValue(jwtProvider.getRefreshTokenKey(userId), refreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);

        return AuthResponseDto.of(accessToken, refreshToken);
    }

    public void discardToken(String authorization, String refreshToken) {
        String accessToken = jwtProvider.getAccessToken(authorization);

        validateToken(refreshToken);
        compareUserId(accessToken, refreshToken);

        String userId = jwtProvider.getSubject(accessToken);
        redisManager.deleteValue(jwtProvider.getRefreshTokenKey(userId));

        long timeout = jwtProvider.getExpirationDate(accessToken);
        redisManager.setValue(accessToken, jwtProvider.getBlackListTokenValue(userId), timeout);
    }

    public AuthResponseDto reissueToken(String refreshToken) {
        validateToken(refreshToken);

        String userId = jwtProvider.getSubject(refreshToken);
        String cachedRefreshToken = redisManager.getValue(jwtProvider.getRefreshTokenKey(userId));

        compareToken(refreshToken, cachedRefreshToken);

        String newAccessToken = jwtProvider.createAccessToken(userId);

        return AuthResponseDto.of(newAccessToken, refreshToken);
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
        if (!jwtProvider.getSubject(accessToken).equals(jwtProvider.getSubject(refreshToken))) {
            throw new BadRequestException(ResponseType.AUTH_NOT_SAME_USER);
        }
    }

}
