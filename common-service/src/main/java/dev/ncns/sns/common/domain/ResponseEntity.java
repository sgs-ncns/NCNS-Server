package dev.ncns.sns.common.domain;

import lombok.Getter;

@Getter
public class ResponseEntity<T> {

    private String responseCode;
    private String message;
    private T data;

    public ResponseEntity() {
    }

    public ResponseEntity(String responseCode, String message, T data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

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

    public static <T> ResponseEntity<T> failureResponse(String responseCode, String message) {
        return new ResponseEntity<>(responseCode, message, null);
    }

    private static String getResponseCode(String port, String code) {
        return port.substring(2) + code;
    }

}
