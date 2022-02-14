package dev.ncns.sns.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowStatus {

    FOLLOW(true),
    UNFOLLOW(false);

    private final Boolean value;

}
