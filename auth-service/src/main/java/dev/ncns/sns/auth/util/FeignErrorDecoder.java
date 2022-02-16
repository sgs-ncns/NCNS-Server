package dev.ncns.sns.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

    private static final String LOGIN_PATH = "/api/user/login";

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        Reader reader = response.body().asReader(StandardCharsets.UTF_8);
        String errorResult = IOUtils.toString(reader);
        String path = response.request().url();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        ResponseEntity errorResponse = objectMapper.readValue(errorResult, ResponseEntity.class);
        int status = response.status();

        if (StringUtils.endsWithIgnoreCase(path, LOGIN_PATH) && isLoginError(errorResponse.getResponseCode())) {
            status = HttpStatus.OK.value();
        }
        return new FeignClientException(status, errorResponse.getResponseCode(), errorResponse.getMessage());
    }

    private boolean isLoginError(String responseCode) {
        String code = responseCode.substring(2);
        String emailErrorCode = ResponseType.USER_NOT_EXIST_EMAIL.getCode();
        String accountNameErrorCode = ResponseType.USER_NOT_EXIST_ACCOUNT_NAME.getCode();
        String passwordErrorCode = ResponseType.USER_NOT_MATCH_PASSWORD.getCode();

        return emailErrorCode.equals(code) || accountNameErrorCode.equals(code) || passwordErrorCode.equals(code);
    }

}
