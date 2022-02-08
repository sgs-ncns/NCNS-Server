package dev.ncns.sns.common.domain;

import lombok.Getter;

@Getter
public class ResponseEntity<T> {

    private final String responseCode;
    private final String message;
    private final T data;

    public ResponseEntity(String port, String code, String message, T data) {
        this.responseCode = getResponseCode(port, code);
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<T> successResponse(String port) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(port, type.getCode(), type.getMessage(), null);
    }

    public static <T> ResponseEntity<T> successResponse(String port, String message) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(port, type.getCode(), message, null);
    }

    public static <T> ResponseEntity<T> successResponse(String port, T data) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(port, type.getCode(), type.getMessage(), data);
    }

    public static <T> ResponseEntity<T> successResponse(String port, String message, T data) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(port, type.getCode(), message, data);
    }

    public static <T> ResponseEntity<T> failureResponse(String port, ResponseType type) {
        return new ResponseEntity<>(port, type.getCode(), type.getMessage(), null);
    }

    public static <T> ResponseEntity<T> failureResponse(String port, ResponseType type, String message) {
        return new ResponseEntity<>(port, type.getCode(), message, null);
    }

    public static <T> ResponseEntity<T> failureResponse(String port, ResponseType type, T data) {
        return new ResponseEntity<>(port, type.getCode(), type.getMessage(), data);
    }

    private static String getResponseCode(String port, String code) {
        return port.substring(2) + code;
    }

}
