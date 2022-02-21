package dev.ncns.sns.post.dto.kafka;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class UpdatePostHashtagRequestDto {

    private final Long postId;
    private final List<String> hashtags;
    private final boolean status;

    @Builder
    private UpdatePostHashtagRequestDto(Long postId, List<String> hashtags, boolean status) {
        this.postId = postId;
        this.hashtags = hashtags;
        this.status = status;
    }

    public static UpdatePostHashtagRequestDto of(Long postId, List<String> hashtags, boolean status) {
        return UpdatePostHashtagRequestDto.builder()
                .postId(postId)
                .hashtags(hashtags)
                .status(status)
                .build();
    }

    public static UpdatePostHashtagRequestDto of(Long postId, String hashtags, boolean status) {
        return UpdatePostHashtagRequestDto.builder()
                .postId(postId)
                .hashtags(Arrays.asList(hashtags.split(",")))
                .status(status)
                .build();
    }

}
