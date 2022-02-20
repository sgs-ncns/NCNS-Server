package dev.ncns.sns.search.dto.request;

import lombok.Getter;

@Getter
public class UserConsumerRequestDto {

    private Long userId;
    private String accountName;
    private String nickname;

    public CreateUserRequestDto convertCreateUser() {
        return new CreateUserRequestDto(userId, accountName, nickname);
    }

    public UpdateUserRequestDto convertUpdateUser() {
        return new UpdateUserRequestDto(userId, accountName, nickname);
    }

}
