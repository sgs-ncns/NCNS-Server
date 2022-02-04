package com.ncns.sns.post.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdatePostRequestDto {

    private final Long postId;
    private final String content;
    private String hashtag;

}