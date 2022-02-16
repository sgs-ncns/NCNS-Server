package dev.ncns.sns.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckResponseDto {

    private final boolean result;

    @Builder
    private CheckResponseDto(boolean result) {
        this.result = result;
    }

    public static CheckResponseDto of(boolean result) {
        return CheckResponseDto.builder()
                .result(result)
                .build();
    }

}
