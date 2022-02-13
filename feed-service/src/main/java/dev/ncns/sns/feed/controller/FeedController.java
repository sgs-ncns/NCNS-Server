package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.FollowUpdateRequestDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import dev.ncns.sns.common.annotation.NonAuthorize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
@RequestMapping(value = "/api/feed")
@RestController
public class FeedController {

    @Value("${server.port}")
    private String port;

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<Void> createFeedDocument(@Validated @RequestBody CreateFeedDocumentRequestDto dto) {
        feedService.createFeedDocument(dto);
        return ResponseEntity.successResponse(port);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Feed>> getFeed(@PathVariable Long userId) {
        try {
            feedService.updateFeedByPull(userId);
        } catch (Exception e) {
            // TODO: 업데이트 실패 리스폰스 처리
        }
        List<Feed> feeds = feedService.getFeeds(userId);
        return ResponseEntity.successResponse(port, feeds);
    }

    @NonAuthorize
    @PostMapping("/update")
    public void updateSubscribeFeed(@RequestBody PostResponseDto dto) {
        feedService.updateFeedByPush(dto);
    }

    @NonAuthorize
    @PostMapping("/follow")
    public void updateFollowingList(@RequestBody FollowUpdateRequestDto dto) {
        feedService.updateFollowings(dto);
    }

    // TODO: subscribe feign endpoint

}