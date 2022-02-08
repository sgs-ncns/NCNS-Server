package dev.ncns.sns.common.exception;

import dev.ncns.sns.common.domain.ResponseType;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseType responseType;

    public BusinessException(ResponseType responseType) {
        this.responseType = responseType;
    }

}
