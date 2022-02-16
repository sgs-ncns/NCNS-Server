package dev.ncns.sns.search.dto.response;

import dev.ncns.sns.search.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String accountName;
    private final String nickname;

    @Builder
    private UserResponseDto(Long userId, String accountName, String nickname) {
        this.userId = userId;
        this.accountName = accountName;
        this.nickname = nickname;
    }

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .accountName(user.getAccountName())
                .nickname(user.getNickname())
                .build();
    }

}
