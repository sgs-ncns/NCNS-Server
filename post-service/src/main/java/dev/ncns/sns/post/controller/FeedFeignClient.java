package dev.ncns.sns.post.controller;

import dev.ncns.sns.post.config.FeignClientConfig;
import dev.ncns.sns.post.dto.response.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Deprecated
@FeignClient(name = "feed-service", path = "/api/feed", configuration = FeignClientConfig.class)
public interface FeedFeignClient {

    @PostMapping("/update/feed")
    void updateSubscribeFeed(@RequestBody PostResponseDto dto);
}