package com.ncns.sns.post.controller;


import com.ncns.sns.post.dto.request.UpdateUserPostCountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @PostMapping("/count/post")
    void updateUserPostCount(@RequestBody UpdateUserPostCountDto dto);
}
