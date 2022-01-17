package dev.ncns.sns.auth.common;

import dev.ncns.sns.auth.util.VariousGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseEntity<T> {

    private final int responseCode;
    private final String message;
    private final T data;

    public static <T> ResponseEntity<T> successResponse(T data) {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(VariousGenerator.getResponseCode(type.getCode()), type.getMessage(), data);
    }

    public static <T> ResponseEntity<T> successResponse() {
        ResponseType type = ResponseType.SUCCESS;
        return new ResponseEntity<>(VariousGenerator.getResponseCode(type.getCode()), type.getMessage(), null);
    }

    public static <T> ResponseEntity<T> failureResponse(ResponseType type) {
        return new ResponseEntity<>(VariousGenerator.getResponseCode(type.getCode()), type.getMessage(), null);
    }

}
