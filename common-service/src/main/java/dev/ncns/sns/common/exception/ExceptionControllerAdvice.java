package dev.ncns.sns.common.exception;

import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.domain.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Value("${server.port}")
    private String port;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public <T> ResponseEntity<T> handleBusinessException(BusinessException exception) {
        printLog(exception);
        return ResponseEntity.failureResponse(port, exception.getResponseType());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public <T> ResponseEntity<T> handleBadRequestException(BadRequestException exception) {
        printLog(exception);
        return ResponseEntity.failureResponse(port, exception.getResponseType());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public <T> ResponseEntity<T> handleNotFoundException(NotFoundException exception) {
        printLog(exception);
        return ResponseEntity.failureResponse(port, exception.getResponseType());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<T> handleException(Exception exception) {
        printLog(exception.getClass().getName(), exception.getMessage());
        exception.printStackTrace();
        return ResponseEntity.failureResponse(port, ResponseType.FAILURE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseEntity<T> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getFieldErrors().stream()
                .map(e -> e.getField() + " - " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        printLog(exception.getClass().getName(), message);
        return ResponseEntity.failureResponse(port, ResponseType.ARGUMENT_NOT_VALID, message);
    }

    private void printLog(BusinessException exception) {
        log.error(String.format("[Error] %s: %s", exception.getClass().getName(), exception.getResponseType().getMessage()));
    }

    private void printLog(String exceptionName, String message) {
        log.error(String.format("[Error] %s: %s", exceptionName, message));
    }

}
