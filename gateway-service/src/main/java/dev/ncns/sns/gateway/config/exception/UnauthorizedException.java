package dev.ncns.sns.gateway.config.exception;

import dev.ncns.sns.gateway.config.domain.ResponseType;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ResponseType responseType) {
        super(responseType);
    }

}
