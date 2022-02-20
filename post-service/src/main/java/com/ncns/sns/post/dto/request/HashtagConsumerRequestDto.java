package com.ncns.sns.post.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HashtagConsumerRequestDto {

    private final Long postId;
    private final List<String> hashtags;

    @Builder
    private HashtagConsumerRequestDto(Long postId, List<String> hashtags) {
        this.postId = postId;
        this.hashtags = hashtags;
    }

    public static HashtagConsumerRequestDto of(Long postId, List<String> hashtags) {
        return HashtagConsumerRequestDto.builder()
                .postId(postId)
                .hashtags(hashtags)
                .build();
    }

    public static HashtagConsumerRequestDto of(Long postId, String hashtags) {
        return HashtagConsumerRequestDto.builder()
                .postId(postId)
                .hashtags(Arrays.stream(hashtags.split(",")).collect(Collectors.toList()))
                .build();
    }

}
