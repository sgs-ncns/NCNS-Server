package dev.ncns.sns.gateway.exception;

import dev.ncns.sns.gateway.domain.ResponseType;

public class NotFoundException extends BusinessException {

    public NotFoundException(ResponseType type) {
        super(type);
    }

}
