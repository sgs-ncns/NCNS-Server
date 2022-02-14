package dev.ncns.sns.common.controller;

import dev.ncns.sns.common.domain.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

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
