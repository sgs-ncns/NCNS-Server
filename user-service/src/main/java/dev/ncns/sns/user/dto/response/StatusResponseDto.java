package dev.ncns.sns.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private final String status;

    @Builder
    private StatusResponseDto(String status) {
        this.status = status;
    }

    public static StatusResponseDto of(String status) {
        return StatusResponseDto.builder()
                .status(status)
                .build();
    }

}
