package dev.ncns.sns.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateAccessAtRequestDto {

    private final Long userId;

    @JsonCreator
    public UpdateAccessAtRequestDto(Long userId) {
        this.userId = userId;
    }

}
