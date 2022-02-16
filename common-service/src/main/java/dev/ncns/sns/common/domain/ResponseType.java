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
    REQUEST_NOT_VALID("02", "유효하지 않는 요청입니다."),
    REQUEST_UNAUTHORIZED("03", "비인증된 요청입니다."),
    JWT_NOT_VALID("04", "토큰 검증에 실패하였습니다."),
    JWT_MALFORMED("05", "위조된 토큰입니다."),
    JWT_UNSUPPORTED("06", "지원하지 않는 토큰입니다."),
    JWT_SIGNATURE("07", "시그니처 검증에 실패한 토큰입니다."),
    JWT_EXPIRED("08", "만료된 토큰입니다."),
    JWT_NULL_OR_EMPTY("09", "토큰이 없거나 값이 비어있습니다."),
    JWT_HEADER_PREFIX("10", "토큰 값은 'Bearer' 로 시작해야 합니다."),

    // AUTH
    AUTH_NULL_TOKEN("11", "토큰을 찾을 수 없습니다."),
    AUTH_NOT_SAME_TOKEN("12", "저장된 토큰과 일치하지 않습니다."),
    AUTH_NOT_SAME_USER("13", "Access와 Refresh 토큰의 사용자가 일치하지 않습니다."),
    AUTH_NOT_FOUND_REDIS_KEY("14", "Redis에서 해당 Key를 찾을 수 없습니다."),
    AUTH_NOT_FOUND_COOKIE_KEY("15", "Cookie에서 해당 Key를 찾을 수 없습니다."),

    // USER
    USER_NOT_EXIST_ID("11", "존재하지 않은 사용자 ID입니다."),
    USER_NOT_EXIST_EMAIL("12", "존재하지 않은 이메일입니다."),
    USER_NOT_EXIST_ACCOUNT_NAME("13", "존재하지 않은 계정 이름입니다."),
    USER_DUPLICATED_EMAIL("14", "중복된 이메일입니다."),
    USER_DUPLICATED_ACCOUNT_NAME("15", "중복된 계정 이름입니다."),
    USER_NOT_MATCH_PASSWORD("16", "비밀번호가 일치하지 않습니다."),
    USER_NOT_MATCH_AUTH_TYPE("17", "가입 인증 유형이 일치하지 않습니다."),

    // POST
    POST_NOT_EXIST("11", "존재하지 않은 게시글입니다."),
    POST_NOT_EXIST_HASHTAG("12", "존재하지 않은 해시태그입니다."),
    POST_NOT_EXIST_COMMENT("13", "존재하지 않은 댓글입니다."),
    POST_NOT_AUTHOR("14", "작성자가 아닙니다."),

    // SEARCH
    SEARCH_NOT_EXIST_USER("11", "존재하지 않은 사용자입니다."),
    ;

    private final String code;
    private final String message;

}
