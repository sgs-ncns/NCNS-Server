package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.FollowUpdateRequestDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.service.FeedService;
import dev.ncns.sns.feed.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/feed")
@RestController
public class FeedController extends ApiController {


    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<Feed>> getFeed() {
        try {
            feedService.updateFeedByPull(SecurityUtil.getCurrentUserId());
        } catch (Exception e) {
            // TODO: 업데이트 실패 리스폰스 처리
        }
        List<Feed> feeds = feedService.getFeeds(SecurityUtil.getCurrentUserId());
        return getSuccessResponse(feeds);
    }

    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> createFeedDocument(@Validated @RequestBody CreateFeedDocumentRequestDto dto) {
        feedService.createFeedDocument(dto);
        return getSuccessResponse();
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