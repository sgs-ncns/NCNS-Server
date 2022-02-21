package dev.ncns.sns.post.dto.response;

import dev.ncns.sns.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSummaryResponseDto {

    private Long postId;
    private Long userId;
    private String imagePath;

    @Builder
    private PostSummaryResponseDto(Long postId, Long userId, String imagePath) {
        this.postId = postId;
        this.userId = userId;
        this.imagePath = imagePath;
    }

    public static PostSummaryResponseDto of(Post post) {
        return PostSummaryResponseDto.builder()
                .postId(post.getId())
                .userId(post.getUserId())
                .imagePath(post.getImagePath())
                .build();
    }

}
