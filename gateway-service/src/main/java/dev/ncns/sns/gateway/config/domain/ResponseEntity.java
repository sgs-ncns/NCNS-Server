package dev.ncns.sns.gateway.config.domain;

import dev.ncns.sns.gateway.util.Constants;
import lombok.Getter;

@Getter
public class ResponseEntity<T> {

    private final String responseCode;
    private final String message;
    private final T data;

    public ResponseEntity(String code, String message, T data) {
        this.responseCode = getResponseCode(code);
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<T> failureResponse(ResponseType type) {
        return new ResponseEntity<>(type.getCode(), type.getMessage(), null);
    }

    private static String getResponseCode(String code) {
        return Constants.port + code;
    }

}
