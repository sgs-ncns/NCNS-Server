package dev.ncns.sns.feed.dto.response;

import dev.ncns.sns.feed.domain.Feed;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long postId;
    private Long userId;
    private String images;
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long commentCount;

    public Feed toEntity() {
        return Feed.builder()
                    .postId(this.postId)
                    .userId(this.userId)
                    .images(this.images)
                    .content(this.content)
                    .createdAt(this.createdAt)
                    .likeCount(this.likeCount)
                    .commentCount(this.commentCount)
                    .build();
    }

}