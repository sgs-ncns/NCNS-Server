package dev.ncns.sns.auth.domain;

import lombok.Getter;

@Getter
public enum AuthType {

    GOOGLE,
    APPLE,
    LOCAL,
    UNKNOWN

}
