package dev.ncns.sns.auth.config;

import dev.ncns.sns.auth.util.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    /**
     * FeignClient API 통신 중 Error가 발생하는 경우에 대해서 Handling 하기 위해
     * ErrorDecoder Interface의 errorDecoder method를 구현하였습니다.
     * 직접 정의한 ErrorDecoder(dev.ncns.sns.auth.util.FeignErrorDecoder)를 return 합니다.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

}
