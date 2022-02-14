package com.ncns.sns.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeStatus {

    LIKE(true),
    DISLIKE(false);

    private final Boolean value;

}