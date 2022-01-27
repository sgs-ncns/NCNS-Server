package dev.ncns.sns.common.domain;

import dev.ncns.sns.common.CodeGenerator;
import lombok.Getter;

@Getter
public class ResponseEntity<T> {

    private final String responseCode;
    private final String message;
    private final T data;

    public ResponseEntity(String responseCode, String message, T data) {
        this.responseCode = CodeGenerator.getResponseCode(responseCode);
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<T> successResponse() {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(type.getCode(), type.getMessage(), null);
    }

    public static <T> ResponseEntity<T> successResponse(String message) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(type.getCode(), message, null);
    }

    public static <T> ResponseEntity<T> successResponse(T data) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(type.getCode(), type.getMessage(), data);
    }

    public static <T> ResponseEntity<T> successResponse(String message, T data) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(type.getCode(), message, data);
    }

    public static <T> ResponseEntity<T> failureResponse(ResponseType type) {
        return new ResponseEntity<>(type.getCode(), type.getMessage(), null);
    }

    public static <T> ResponseEntity<T> failureResponse(ResponseType type, T data) {
        return new ResponseEntity<>(type.getCode(), type.getMessage(), data);
    }

}
