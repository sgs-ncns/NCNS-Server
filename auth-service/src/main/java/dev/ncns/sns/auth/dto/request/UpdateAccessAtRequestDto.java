package dev.ncns.sns.auth.dto.request;

import lombok.Getter;

@Getter
public class UpdateAccessAtRequestDto {

    private final Long userId;

    private UpdateAccessAtRequestDto(Long userId) {
        this.userId = userId;
    }

    public static UpdateAccessAtRequestDto of(Long userId) {
        return new UpdateAccessAtRequestDto(userId);
    }

}
