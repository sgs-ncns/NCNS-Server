package dev.ncns.sns.feed.dto.response;

import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long userId;
    private Long postId;
    private Boolean liking;

}
