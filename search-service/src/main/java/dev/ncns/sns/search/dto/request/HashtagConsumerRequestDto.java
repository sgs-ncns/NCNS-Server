package dev.ncns.sns.search.dto.request;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HashtagConsumerRequestDto {

    private Long postId;
    private List<String> hashtags;

    public List<CreateHashtagRequestDto> convertCreateHashtags() {
        return hashtags.stream()
                .map(content -> new CreateHashtagRequestDto(content, postId))
                .collect(Collectors.toList());
    }

}
