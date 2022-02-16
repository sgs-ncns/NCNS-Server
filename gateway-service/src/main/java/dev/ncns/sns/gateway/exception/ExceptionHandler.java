package dev.ncns.sns.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ncns.sns.gateway.domain.ResponseEntity;
import dev.ncns.sns.gateway.domain.ResponseType;
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

        /**
         * Exception 상황에 맞게 응답하기 위해 분기처리 하였습니다.
         */
        if (ex instanceof BadRequestException) {
            printLog((BusinessException) ex);
            httpStatus = HttpStatus.BAD_REQUEST;
            responseEntity = ResponseEntity.failureResponse(((BusinessException) ex).getResponseType());
        } else if (ex instanceof UnauthorizedException) {
            printLog((BusinessException) ex);
            httpStatus = HttpStatus.UNAUTHORIZED;
            responseEntity = ResponseEntity.failureResponse(((BusinessException) ex).getResponseType());
        } else {
            printLog(ex.getClass().getName(), ex.getMessage());
            ex.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseEntity = ResponseEntity.failureResponse(ResponseType.FAILURE);
        }

        /**
         * 응답 객체를 ObjectMapper를 이용하여 감싼 뒤,
         * 응답에 HttpStatus, Header 정보를 함께 담아 응답으로 내려줍니다.
         */
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
