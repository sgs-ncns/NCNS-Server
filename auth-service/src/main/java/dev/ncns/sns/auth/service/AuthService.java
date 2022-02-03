package dev.ncns.sns.auth.service;

import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.util.RedisManager;
import dev.ncns.sns.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.util")
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisManager redisManager;

    public AuthResponseDto issueToken(LoginResponseDto loginResponse) {
        String userId = loginResponse.getId().toString();
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        redisManager.setValue(jwtProvider.getRefreshTokenKey(userId), refreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);

        return AuthResponseDto.of(accessToken, refreshToken);
    }

    public void discardToken(String authorization, String refreshToken) {
        String accessToken = jwtProvider.getAccessToken(authorization);

        validateToken(accessToken, refreshToken);
        compareUserId(accessToken, refreshToken);

        String userId = jwtProvider.getSubject(accessToken);
        redisManager.deleteValue(jwtProvider.getRefreshTokenKey(userId));

        long timeout = jwtProvider.getExpirationDate(accessToken);
        redisManager.setValue(accessToken, jwtProvider.getBlackListTokenValue(userId), timeout);
    }

    public AuthResponseDto reissueToken(String authorization, String refreshToken) {
        String accessToken = jwtProvider.getAccessToken(authorization);

        validateToken(accessToken, refreshToken);
        compareUserId(accessToken, refreshToken);

        String userId = jwtProvider.getSubject(accessToken);
        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        redisManager.setValue(jwtProvider.getRefreshTokenKey(userId), newRefreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);

        return AuthResponseDto.of(newAccessToken, newRefreshToken);
    }

    private void validateToken(String accessToken, String refreshToken) {
        if (!jwtProvider.validateToken(accessToken)) {
            if (!jwtProvider.validateToken(refreshToken)) {
                throw new IllegalArgumentException("Not Valid Token"); // TODO: Exception Handling
            }
        }
    }

    private void compareUserId(String accessToken, String refreshToken) {
        if (!jwtProvider.getSubject(accessToken).equals(jwtProvider.getSubject(refreshToken))) {
            throw new IllegalArgumentException("Not Same UserId");
        }
    }

}
