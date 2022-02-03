package com.ncns.sns.post.dto.request;

import lombok.Getter;

@Getter
public class UpdatePostRequestDto {

    private Long postId;
    private String content;
    private String hashtag;

}