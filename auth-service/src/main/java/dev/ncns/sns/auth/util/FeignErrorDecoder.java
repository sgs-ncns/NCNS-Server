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

    /**
     * FeignClient API 호출 시 실패 응답이 온 경우 Handling 됩니다.
     */
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        /**
         * Response Body에 담긴 json 형태의 응답을 읽어 String으로 변환합니다.
         * Json 문자열을 ObjectMapper를 이용하여 변환합니다.
         */
        Reader reader = response.body().asReader(StandardCharsets.UTF_8);
        String errorResult = IOUtils.toString(reader);
        String path = response.request().url();
        ObjectMapper objectMapper = new ObjectMapper();

        /**
         * Json 문자열은 SnakeCase로 네이밍 되어있어 ObjectMapper에 네이밍 규칙을 등록해준 후,
         * Json을 POJO(ResponseEntity)로 변환합니다.
         */
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        ResponseEntity errorResponse = objectMapper.readValue(errorResult, ResponseEntity.class);
        int status = response.status();

        /**
         * Login error(email or accountName or password 불일치)인 경우에 대해서,
         * error messge는 전달하되 200 응답이 내려갈 수 있도록 처리합니다.
         */
        if (StringUtils.endsWithIgnoreCase(path, LOGIN_PATH) && isLoginError(errorResponse.getResponseCode())) {
            status = HttpStatus.OK.value();
        }
        /**
         * FeignClientException을 터뜨려 호출했던 API에서 발생한 Error를 그대로 전달합니다.
         */
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
