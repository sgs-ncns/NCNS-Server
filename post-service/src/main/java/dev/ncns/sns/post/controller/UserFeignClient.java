package dev.ncns.sns.post.controller;

import dev.ncns.sns.post.config.FeignClientConfig;
import dev.ncns.sns.post.dto.request.UpdateUserPostCountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/user", configuration = FeignClientConfig.class)
public interface UserFeignClient {

    @PostMapping("/count/post")
    void updateUserPostCount(@RequestBody UpdateUserPostCountDto updateUserPostCountDto);

}
