package dev.ncns.sns.gateway.exception;

import dev.ncns.sns.gateway.domain.ResponseType;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseType responseType;

    public BusinessException(ResponseType responseType) {
        this.responseType = responseType;
    }

}
