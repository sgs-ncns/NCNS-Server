package dev.ncns.sns.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthLoginResponseDto {

    private final String accessToken;
    private final String refreshToken;
    private final Long userId;
    private final String accountName;

    @Builder
    public AuthLoginResponseDto(String accessToken, String refreshToken, Long userId, String accountName) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.accountName = accountName;
    }

    public static AuthLoginResponseDto of(AuthResponseDto authResponseDto, LoginResponseDto loginResponseDto) {
        return AuthLoginResponseDto.builder()
                .accessToken(authResponseDto.getAccessToken())
                .refreshToken(authResponseDto.getRefreshToken())
                .userId(loginResponseDto.getUserId())
                .accountName(loginResponseDto.getAccountName())
                .build();
    }

}
