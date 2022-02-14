package dev.ncns.sns.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscribeStatus {

    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe");

    private final String value;

}
