package dev.ncns.sns.search.dto.kafka;

import dev.ncns.sns.search.dto.request.CreateUserRequestDto;
import dev.ncns.sns.search.dto.request.UpdateUserRequestDto;
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
