package dev.ncns.sns.search.dto.request;

import dev.ncns.sns.search.domain.Hashtag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateHashtagRequestDto {

    private final String content;
    private final Long postId;

    public Hashtag toEntity() {
        return Hashtag.builder()
                .content(content)
                .postIdList(List.of(postId))
                .build();
    }

}
