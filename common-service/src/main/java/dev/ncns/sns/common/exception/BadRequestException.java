package dev.ncns.sns.common.exception;

import dev.ncns.sns.common.domain.ResponseType;

public class BadRequestException extends BusinessException {

    public BadRequestException(ResponseType type) {
        super(type);
    }

}
