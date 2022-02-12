package dev.ncns.sns.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException {

    private final HttpStatus status;
    private final String responseCode;
    private final String message;

    public FeignClientException(int status, String responseCode, String message) {
        this.status = HttpStatus.valueOf(status);
        this.responseCode = responseCode;
        this.message = message;
    }

}
