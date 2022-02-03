package com.ncns.sns.post.dto.response;

import com.ncns.sns.post.domain.Comments;
import com.ncns.sns.post.domain.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailResponseDto {

    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private List<Comments> commentsList; // TODO:: pagination

    @Builder
    private PostDetailResponseDto(Post post, List<Comments> commentsList) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentsList = commentsList;
    }

    public static PostDetailResponseDto of(Post post, List<Comments> commentsList) {
        return PostDetailResponseDto.builder()
                .post(post)
                .commentsList(commentsList)
                .build();
    }
}
