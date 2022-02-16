package dev.ncns.sns.common.controller;

import dev.ncns.sns.common.domain.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

/**
 * 다른 모듈 Controller에서 공통으로 사용하는 부분을 추상 Controller 클래스에 정의하였습니다.
 * (빈 등록, port 값 주입, successResponse())
 */

/**
 * 다른 모듈 컨트롤러에서 해당 모듈(common)에 정의된 ExceptionControllerAdvice에 의해
 * handling 될 수 있도록 "dev.ncns.sns.common.exception" 클래스들을 빈으로 등록해줍니다.
 */
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
public abstract class ApiController {

    @Value("${server.port}")
    private String port;

    public <T> ResponseEntity<T> getSuccessResponse() {
        return ResponseEntity.successResponse(port);
    }

    public <T> ResponseEntity<T> getSuccessResponse(T data) {
        return ResponseEntity.successResponse(port, data);
    }

    public <T> ResponseEntity<T> getSuccessResponse(String message, T data) {
        return ResponseEntity.successResponse(port, message, data);
    }

}
