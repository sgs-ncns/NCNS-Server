package dev.ncns.sns.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResponseDto {

    private final String accessToken;
    private final String refreshToken;

    @Builder
    private AuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthResponseDto of(String accessToken, String refreshToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
