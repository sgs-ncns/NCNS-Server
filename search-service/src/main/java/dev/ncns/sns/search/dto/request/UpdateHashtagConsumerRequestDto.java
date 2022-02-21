package dev.ncns.sns.search.dto.request;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UpdateHashtagConsumerRequestDto {

    private Long postId;
    private List<String> hashtags;
    private boolean status;

    public List<UpdateHashtagRequestDto> convertUpdateHashtags() {
        return hashtags.stream()
                .map(content -> new UpdateHashtagRequestDto(content, postId, status))
                .collect(Collectors.toList());
    }

}
