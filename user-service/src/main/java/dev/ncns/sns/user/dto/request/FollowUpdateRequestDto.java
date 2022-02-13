package dev.ncns.sns.user.dto.request;

import lombok.Builder;

public class FollowUpdateRequestDto {

    private Long userId;
    private Long targetId;
    private Boolean isAdd;

    @Builder
    public FollowUpdateRequestDto(Long userId, Long targetId, Boolean isAdd) {
        this.userId = userId;
        this.targetId = targetId;
        this.isAdd = isAdd;
    }

    public static FollowUpdateRequestDto of(Long userId, Long targetId, Boolean isAdd) {
        return FollowUpdateRequestDto.builder()
                .userId(userId)
                .targetId(targetId)
                .isAdd(isAdd)
                .build();
    }
}