package dev.ncns.sns.search.dto.response;

import dev.ncns.sns.search.domain.Hashtag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HashtagResponseDto {

    private final String content;
    private final int count;
    private final List<Long> postIdList;

    @Builder
    private HashtagResponseDto(String content, int count, List<Long> postIdList) {
        this.content = content;
        this.count = count;
        this.postIdList = postIdList;
    }

    public static HashtagResponseDto of(Hashtag hashtag) {
        return HashtagResponseDto.builder()
                .content(hashtag.getContent())
                .count(hashtag.getPostIdList().size())
                .postIdList(hashtag.getPostIdList())
                .build();
    }

}
