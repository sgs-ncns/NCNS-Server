package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.config.FeignClientConfig;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import dev.ncns.sns.auth.dto.request.UpdateAccessAtRequestDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.common.domain.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Error Handling을 위해 정의한 FeignClientConfig를 등록하여 줍니다.
 */
@FeignClient(name = "user-service", path = "/api/user", configuration = FeignClientConfig.class)
public interface UserFeignClient {

    @PostMapping(value = "/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest);

    @PutMapping(value = "/access")
    ResponseEntity<Void> updateUserAccessAt(@RequestBody UpdateAccessAtRequestDto updateAccessAtRequest);

}
