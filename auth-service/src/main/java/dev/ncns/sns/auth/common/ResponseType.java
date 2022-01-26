package dev.ncns.sns.auth.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    SUCCESS("00", "success :)"),
    FAILURE("01", "failure :(");

    private final String code;
    private final String message;

}
