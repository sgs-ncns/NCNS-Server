package dev.ncns.sns.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowStatus {

    FOLLOW("follow"),
    UNFOLLOW("unfollow");

    private final String value;

}
