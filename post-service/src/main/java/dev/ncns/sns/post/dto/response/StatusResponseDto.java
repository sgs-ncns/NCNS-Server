package dev.ncns.sns.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private final Boolean status;

    @Builder
    private StatusResponseDto(Boolean status) {
        this.status = status;
    }

    public static StatusResponseDto of(Boolean status) {
        return StatusResponseDto.builder()
                .status(status)
                .build();
    }

}
