package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.config.FeignClientConfig;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.common.domain.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserFeignClient {

    @PostMapping(value = "/api/user/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest);

}
