package dev.ncns.sns.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    USER_PROFILE("userProfile", 60, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

}
