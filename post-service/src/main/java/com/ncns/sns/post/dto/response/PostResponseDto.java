package com.ncns.sns.post.dto.response;

import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.domain.PostCount;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long postId;
    private Long userId;
    private String images;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;

    @Builder
    private PostResponseDto(Post post, PostCount postCount) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.images = post.getImage();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.likeCount = postCount.getLikeCount();
        this.commentCount = postCount.getCommentCount();
    }

    public static PostResponseDto of(Post post, PostCount postCount) {
        return PostResponseDto.builder()
                .post(post)
                .postCount(postCount)
                .build();
    }

}