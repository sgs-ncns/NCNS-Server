package dev.ncns.sns.auth.service;

import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.util.JwtProvider;
import dev.ncns.sns.auth.util.RedisManager;
import dev.ncns.sns.auth.util.VariousGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    public final RedisManager redisManager;

    public AuthResponseDto issueToken(LoginResponseDto loginResponse) {
        String userId = loginResponse.getId().toString();
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        redisManager.setValue(VariousGenerator.getRefreshTokenKey(userId), refreshToken, JwtProvider.REFRESH_TOKEN_VALIDITY);

        return AuthResponseDto.of(accessToken, refreshToken);
    }

}
