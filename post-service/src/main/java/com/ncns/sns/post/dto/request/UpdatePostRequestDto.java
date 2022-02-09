package com.ncns.sns.post.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class UpdatePostRequestDto {

    private final Long postId;
    private final String content;
    private List<String> hashtag;
    private List<Long> usertag;

}