package dev.ncns.sns.feed.controller;

import dev.ncns.sns.feed.dto.request.FeedPullRequestDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "post-service", path = "/api/post")
public interface PostFeignClient {

    @PostMapping("/feed")
    List<PostResponseDto> getNewFeeds(@RequestBody FeedPullRequestDto dto);
}
