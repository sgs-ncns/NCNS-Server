package com.ncns.sns.post.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateCommentRequestDto {
    private final Long commentId;
    private final String content;
}
