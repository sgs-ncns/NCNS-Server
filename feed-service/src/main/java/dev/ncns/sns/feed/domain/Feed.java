package dev.ncns.sns.feed.domain;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Feed {

    private Long postId;
    private Long userId;
    private String images;
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long commentCount;

}