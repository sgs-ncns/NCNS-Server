package com.ncns.sns.post.controller;

import com.ncns.sns.post.config.FeignClientConfig;
import com.ncns.sns.post.dto.response.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feed-service", path = "/api/feed", configuration = FeignClientConfig.class)
public interface FeedFeignClient {

    @PostMapping("/update/feed")
    void updateSubscribeFeed(@RequestBody PostResponseDto dto);
}
