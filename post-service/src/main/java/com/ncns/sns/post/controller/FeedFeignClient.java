package com.ncns.sns.post.controller;

import com.ncns.sns.post.dto.response.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feed-service", path = "/api/feed")
public interface FeedFeignClient {

    @PostMapping("/update")
    void updateSubscribeFeed(@RequestBody PostResponseDto dto);
}
