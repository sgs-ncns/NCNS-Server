package dev.ncns.sns.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeResponseDto {

    private final Long userId;
    private final Long postId;
    private final Boolean liking;

    @Builder
    private LikeResponseDto(Long userId, Long postId, Boolean liking) {
        this.userId = userId;
        this.postId = postId;
        this.liking = liking;
    }

    public static LikeResponseDto of(Long userId, Long postId, Boolean liking) {
        return LikeResponseDto.builder()
                .userId(userId)
                .postId(postId)
                .liking(liking)
                .build();
    }

}
