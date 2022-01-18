package dev.ncns.sns.user.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseEntity<T> {
    private final int responseCode;
    private final String message;
    private final T data;
}
