package dev.ncns.sns.search.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateHashtagRequestDto {

    private final String content;
    private final Long postId;
    private final boolean status; // true:add, false:remove

}
