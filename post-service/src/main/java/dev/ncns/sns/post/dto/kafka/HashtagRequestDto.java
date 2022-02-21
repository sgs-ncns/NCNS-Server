package dev.ncns.sns.post.dto.kafka;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class HashtagRequestDto {

    private final Long postId;
    private final List<String> hashtags;

    @Builder
    private HashtagRequestDto(Long postId, List<String> hashtags) {
        this.postId = postId;
        this.hashtags = hashtags;
    }

    public static HashtagRequestDto of(Long postId, List<String> hashtags) {
        return HashtagRequestDto.builder()
                .postId(postId)
                .hashtags(hashtags)
                .build();
    }

    public static HashtagRequestDto of(Long postId, String hashtags) {
        return HashtagRequestDto.builder()
                .postId(postId)
                .hashtags(Arrays.asList(hashtags.split(",")))
                .build();
    }

}
