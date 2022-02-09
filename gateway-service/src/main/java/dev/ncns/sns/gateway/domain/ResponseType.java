package dev.ncns.sns.gateway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    SUCCESS("00", "success :)"),
    FAILURE("99", "failure :("),

    ARGUMENT_NOT_VALID("01", "Argument 유효성 검증에 실패하였습니다."),
    REQUEST_NOT_VALID("02", "유효하지 않는 요청입니다."),
    REQUEST_UNAUTHORIZED("03", "비인증된 요청입니다."),
    JWT_NOT_VALID("04", "토큰 검증에 실패하였습니다."),
    JWT_MALFORMED("05", "위조된 토큰입니다."),
    JWT_UNSUPPORTED("06", "지원하지 않는 토큰입니다."),
    JWT_SIGNATURE("07", "시그니처 검증에 실패한 토큰입니다."),
    JWT_EXPIRED("08", "만료된 토큰입니다."),
    JWT_NULL_OR_EMPTY("09", "토큰이 없거나 값이 비어있습니다."),
    JWT_HEADER_PREFIX("10", "토큰 값은 'Bearer' 로 시작해야 합니다."),

    // GATEWAY
    GATEWAY_BLACK_LIST_TOKEN("11", "이미 로그아웃된 사용자입니다. 다시 로그인해주세요.");

    private final String code;
    private final String message;

}
