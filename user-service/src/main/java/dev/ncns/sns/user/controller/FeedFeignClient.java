package dev.ncns.sns.user.controller;

import dev.ncns.sns.user.config.FeignClientConfig;
import dev.ncns.sns.user.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.user.dto.request.UpdateListRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feed-service", path = "/api/feed", configuration = FeignClientConfig.class)
public interface FeedFeignClient {

    @PostMapping
    void createFeedDocument(@RequestBody CreateFeedDocumentRequestDto dto) ;

    @PostMapping("/update/list")
    void updateList(@RequestBody UpdateListRequestDto dto);

}
