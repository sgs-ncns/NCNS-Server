package dev.ncns.sns.search.dto.request;

import dev.ncns.sns.search.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateUserRequestDto {

    private final Long userId;
    private final String accountName;
    private final String nickname;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .accountName(accountName)
                .nickname(nickname)
                .build();
    }

}
