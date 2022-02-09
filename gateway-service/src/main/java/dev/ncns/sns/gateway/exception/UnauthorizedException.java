package dev.ncns.sns.gateway.exception;

import dev.ncns.sns.gateway.domain.ResponseType;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ResponseType responseType) {
        super(responseType);
    }

}
