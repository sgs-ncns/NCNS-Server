package dev.ncns.sns.user.domain;

import lombok.Getter;

@Getter
public enum Status {
    USER,
    ADMIN,
    BLACK   // 신고 누적으로 차단된 사용자
}