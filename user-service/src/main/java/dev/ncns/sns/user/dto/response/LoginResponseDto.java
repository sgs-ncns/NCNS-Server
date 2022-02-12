package dev.ncns.sns.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long userId;
    private final String accountName;

    @Builder
    public LoginResponseDto(Long userId, String accountName) {
        this.userId = userId;
        this.accountName = accountName;
    }

    public static LoginResponseDto of(Long userId, String accountName) {
        return LoginResponseDto.builder()
                .userId(userId)
                .accountName(accountName)
                .build();
    }

}
