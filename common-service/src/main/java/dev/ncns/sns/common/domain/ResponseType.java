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
    JWT_NOT_VALID("02", "토큰 검증에 실패하였습니다."),
    JWT_MALFORMED("03", "위조된 토큰입니다."),
    JWT_UNSUPPORTED("04", "지원하지 않는 토큰입니다."),
    JWT_SIGNATURE("05", "시그니처 검증에 실패한 토큰입니다."),
    JWT_EXPIRED("06", "만료된 토큰입니다."),
    JWT_NULL_OR_EMPTY("07", "토큰이 없거나 값이 비어있습니다."),
    JWT_HEADER_PREFIX("08", "토큰 값은 'Bearer' 로 시작해야 합니다."),

    // AUTH
    AUTH_NULL_TOKEN("11", "토큰을 찾을 수 없습니다."),
    AUTH_NOT_SAME_TOKEN("12", "저장된 토큰과 일치하지 않습니다."),
    AUTH_NOT_SAME_USER("13", "Access와 Refresh 토큰의 사용자가 일치하지 않습니다."),
    AUTH_NOT_FOUND_REDIS_KEY("14", "Redis에서 해당 Key를 찾을 수 없습니다."),
    AUTH_NOT_FOUND_COOKIE_KEY("15", "Cookie에서 해당 Key를 찾을 수 없습니다."),

    // USER
    USER_NOT_FOUND_ID("11", "존재하지 않은 id입니다.");

    private final String code;
    private final String message;

}
