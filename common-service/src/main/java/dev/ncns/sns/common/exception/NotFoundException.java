package dev.ncns.sns.common.exception;

import dev.ncns.sns.common.domain.ResponseType;

public class NotFoundException extends BusinessException {

    public NotFoundException(ResponseType type) {
        super(type);
    }

}
