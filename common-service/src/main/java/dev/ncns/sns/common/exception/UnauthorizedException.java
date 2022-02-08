package dev.ncns.sns.common.exception;

import dev.ncns.sns.common.domain.ResponseType;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ResponseType responseType) {
        super(responseType);
    }

}
