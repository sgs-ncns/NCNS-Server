package com.ncns.sns.post.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UpdateHashtagConsumerRequestDto {

    private final Long postId;
    private final List<String> hashtags;
    private final boolean status;

    @Builder
    private UpdateHashtagConsumerRequestDto(Long postId, List<String> hashtags, boolean status) {
        this.postId = postId;
        this.hashtags = hashtags;
        this.status = status;
    }

    public static UpdateHashtagConsumerRequestDto of(Long postId, List<String> hashtags, boolean status) {
        return UpdateHashtagConsumerRequestDto.builder()
                .postId(postId)
                .hashtags(hashtags)
                .status(status)
                .build();
    }

    public static UpdateHashtagConsumerRequestDto of(Long postId, String hashtags, boolean status) {
        return UpdateHashtagConsumerRequestDto.builder()
                .postId(postId)
                .hashtags(Arrays.stream(hashtags.split(",")).collect(Collectors.toList()))
                .status(status)
                .build();
    }

}
