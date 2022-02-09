package dev.ncns.sns.gateway.exception;

import dev.ncns.sns.gateway.domain.ResponseType;

public class BadRequestException extends BusinessException {

    public BadRequestException(ResponseType type) {
        super(type);
    }

}
