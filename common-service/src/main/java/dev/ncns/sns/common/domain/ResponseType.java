package dev.ncns.sns.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    SUCCESS("00", "success :)"),
    FAILURE("01", "failure :("),

    // Auth
    AUTH_VALIDATION_FAILURE("01", "Validation failure"),

    // User
    USER_VALIDATION_FAILURE("01", "Validation failure");

    private final String code;
    private final String message;

}
