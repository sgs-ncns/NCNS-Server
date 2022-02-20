package dev.ncns.sns.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserConsumerRequestDto {

    private final Long userId;
    private final String accountName;
    private final String nickname;

    @Builder
    private UserConsumerRequestDto(Long userId, String accountName, String nickname) {
        this.userId = userId;
        this.accountName = accountName;
        this.nickname = nickname;
    }

    public static UserConsumerRequestDto of(Long userId, String accountName, String nickname) {
        return UserConsumerRequestDto.builder()
                .userId(userId)
                .accountName(accountName)
                .nickname(nickname)
                .build();
    }

}
