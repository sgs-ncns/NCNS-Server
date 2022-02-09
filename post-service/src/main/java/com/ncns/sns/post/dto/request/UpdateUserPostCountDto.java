package com.ncns.sns.post.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserPostCountDto {

    private final Long userId;
    private final boolean isUp;

}
