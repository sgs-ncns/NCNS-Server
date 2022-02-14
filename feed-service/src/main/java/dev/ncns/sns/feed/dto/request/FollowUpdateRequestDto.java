package dev.ncns.sns.feed.dto.request;

import lombok.Getter;

@Getter
public class FollowUpdateRequestDto {
    private Long userId;
    private Long targetId;
    private Boolean isAdd;
}
