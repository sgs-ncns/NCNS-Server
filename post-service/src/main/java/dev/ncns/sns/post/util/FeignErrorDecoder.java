package dev.ncns.sns.post.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        Reader reader = response.body().asReader(StandardCharsets.UTF_8);
        String errorResult = IOUtils.toString(reader);
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        ResponseEntity errorResponse = objectMapper.readValue(errorResult, ResponseEntity.class);

        return new FeignClientException(response.status(), errorResponse.getResponseCode(), errorResponse.getMessage());
    }

}
