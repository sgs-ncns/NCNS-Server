package dev.ncns.sns.user.controller;

import dev.ncns.sns.user.dto.request.FollowUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feed-service", path = "/api/feed")
public interface FeedFeignClient {

    @PostMapping("/follow")
    void updateFollowingList(@RequestBody FollowUpdateRequestDto dto);

}
