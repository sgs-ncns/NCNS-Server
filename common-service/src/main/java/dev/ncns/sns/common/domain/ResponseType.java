package dev.ncns.sns.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    SUCCESS("00", "success :)"),
    FAILURE("99", "failure :("),

    // COMMON
    ARGUMENT_NOT_VALID("01", "Argument 유효성 검증에 실패하였습니다."),
    NOT_VALID_TOKEN("02", "토큰 검증에 실패하였습니다."),

    // Auth
    AUTH_NULL_TOKEN("11", "토큰을 찾을 수 없습니다."),

    // User
    USER_NOT_FOUND_ID("11", "존재하지 않은 id입니다.");

    private final String code;
    private final String message;

}
