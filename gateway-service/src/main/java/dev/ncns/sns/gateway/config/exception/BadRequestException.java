package dev.ncns.sns.gateway.config.exception;

import dev.ncns.sns.gateway.config.domain.ResponseType;
import lombok.Getter;

@Getter
public class BadRequestException extends BusinessException {

    public BadRequestException(ResponseType type) {
        super(type);
    }

}
