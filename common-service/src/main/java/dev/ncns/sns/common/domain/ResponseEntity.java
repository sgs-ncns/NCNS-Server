package dev.ncns.sns.common.domain;

import lombok.Getter;

@Getter
public class ResponseEntity<T> {

    private String responseCode;
    private String message;
    /**
     * Generic 타입으로 정의하여 어떤 타입의 Data든 응답할 수 있게 하였습니다.
     */
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

    /**
     * 다양한 파라미터 타입을 받을 수 있도록 오버로딩 하였습니다.
     */

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

    /**
     * 각 서버의 port 번호 끝 2자리와 Response Type별 코드 번호를 조합하여
     * 클라이언트 팀원들이 어느 서버에서 어떤 응답을 보냈는 지 알 수 있도록 규칙을 정하였습니다.
     */
    private static String getResponseCode(String port, String code) {
        return port.substring(2) + code;
    }

}
