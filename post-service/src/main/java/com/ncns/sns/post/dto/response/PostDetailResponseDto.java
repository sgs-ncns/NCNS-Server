package com.ncns.sns.post.dto.response;

import com.ncns.sns.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailResponseDto {

    private final Long postId;
    private final Long userId;
    private final String accountName;
    private final String imagePath;
    private final String content;
    private final LocalDateTime createdAt;
    private final List<CommentResponseDto> commentList; // TODO:: pagination

    @Builder
    private PostDetailResponseDto(Post post, List<CommentResponseDto> commentList) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.accountName = post.getAccountName();
        this.imagePath = post.getImagePath();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentList = commentList;
    }

    public static PostDetailResponseDto of(Post post, List<CommentResponseDto> commentList) {
        return PostDetailResponseDto.builder()
                .post(post)
                .commentList(commentList)
                .build();
    }

}
