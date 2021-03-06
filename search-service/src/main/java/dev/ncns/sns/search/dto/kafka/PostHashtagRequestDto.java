package dev.ncns.sns.search.dto.kafka;

import dev.ncns.sns.search.dto.request.CreateHashtagRequestDto;
import dev.ncns.sns.search.dto.request.UpdateHashtagRequestDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostHashtagRequestDto {

    private Long postId;
    private List<String> hashtags;

    public List<CreateHashtagRequestDto> convertCreateHashtags() {
        return hashtags.stream()
                .map(content -> new CreateHashtagRequestDto(content, postId))
                .collect(Collectors.toList());
    }

    public List<UpdateHashtagRequestDto> convertUpdateHashtags(boolean status) {
        return hashtags.stream()
                .map(content -> new UpdateHashtagRequestDto(content, postId, status))
                .collect(Collectors.toList());
    }

}
