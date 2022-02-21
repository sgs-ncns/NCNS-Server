package dev.ncns.sns.post.dto.kafka;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class PostHashtagRequestDto {

    private final Long postId;
    private final List<String> hashtags;

    @Builder
    private PostHashtagRequestDto(Long postId, List<String> hashtags) {
        this.postId = postId;
        this.hashtags = hashtags;
    }

    public static PostHashtagRequestDto of(Long postId, List<String> hashtags) {
        return PostHashtagRequestDto.builder()
                .postId(postId)
                .hashtags(hashtags)
                .build();
    }

    public static PostHashtagRequestDto of(Long postId, String hashtags) {
        return PostHashtagRequestDto.builder()
                .postId(postId)
                .hashtags(Arrays.asList(hashtags.split(",")))
                .build();
    }

}
