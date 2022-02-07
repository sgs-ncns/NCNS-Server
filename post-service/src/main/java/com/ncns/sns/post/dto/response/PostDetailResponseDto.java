package com.ncns.sns.post.dto.response;

import com.ncns.sns.post.domain.Comment;
import com.ncns.sns.post.domain.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailResponseDto {

    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private List<Comment> commentList; // TODO:: pagination

    @Builder
    private PostDetailResponseDto(Post post, List<Comment> commentList) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentList = commentList;
    }

    public static PostDetailResponseDto of(Post post, List<Comment> commentList) {
        return PostDetailResponseDto.builder()
                .post(post)
                .commentList(commentList)
                .build();
    }
}
