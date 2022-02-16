package dev.ncns.sns.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscribeStatus {

    SUBSCRIBE(true),
    UNSUBSCRIBE(false);

    private final Boolean value;

}
