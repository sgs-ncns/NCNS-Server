package dev.ncns.sns.gateway.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ncns.sns.gateway.config.domain.ResponseEntity;
import dev.ncns.sns.gateway.config.domain.ResponseType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Slf4j
@Component
public class ExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public ExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        HttpStatus httpStatus;
        ResponseEntity<Void> responseEntity;

        if (ex instanceof BadRequestException) {
            printLog((BusinessException) ex);
            httpStatus = HttpStatus.BAD_REQUEST;
            responseEntity = ResponseEntity.failureResponse(((BadRequestException) ex).getResponseType());
        } else if (ex instanceof UnauthorizedException) {
            printLog((BusinessException) ex);
            httpStatus = HttpStatus.UNAUTHORIZED;
            responseEntity = ResponseEntity.failureResponse(((UnauthorizedException) ex).getResponseType());
        } else if (ex instanceof BusinessException) {
            printLog((BusinessException) ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseEntity = ResponseEntity.failureResponse(((BusinessException) ex).getResponseType());
        } else {
            printLog(ex.getClass().getName(), ex.getMessage());
            ex.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseEntity = ResponseEntity.failureResponse(ResponseType.FAILURE);
        }

        DataBuffer dataBuffer = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(responseEntity));
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().getHeaders().setDate(ZonedDateTime.now());

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private void printLog(BusinessException exception) {
        log.error(String.format("[Error] %s: %s", exception.getClass().getName(), exception.getResponseType().getMessage()));
    }

    private void printLog(String exceptionName, String message) {
        log.error(String.format("[Error] %s: %s", exceptionName, message));
    }

}
