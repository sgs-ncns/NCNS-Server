package dev.ncns.sns.post.dto.response;

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
    private final Long likeCount;
    private boolean isLiking;
    private final List<CommentResponseDto> commentList;

    @Builder
    private PostDetailResponseDto(PostResponseDto dto, List<CommentResponseDto> commentList) {
        this.postId = dto.getPostId();
        this.userId = dto.getUserId();
        this.accountName = dto.getAccountName();
        this.imagePath = dto.getImagePath();
        this.content = dto.getContent();
        this.likeCount = dto.getLikeCount();
        this.isLiking = false;
        this.createdAt = dto.getCreatedAt();
        this.commentList = commentList;
    }

    public static PostDetailResponseDto of(PostResponseDto dto, List<CommentResponseDto> commentList) {
        return PostDetailResponseDto.builder()
                .dto(dto)
                .commentList(commentList)
                .build();
    }

    public void liking() {
        this.isLiking = true;
    }

}
