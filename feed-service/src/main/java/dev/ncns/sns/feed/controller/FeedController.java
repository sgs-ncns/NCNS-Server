package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.FeedResponseDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.dto.response.SubscribingFeedResponseDto;
import dev.ncns.sns.feed.service.FeedService;
import dev.ncns.sns.feed.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<FeedResponseDto> getFeed() {
        Long currentUser = SecurityUtil.getCurrentUserId();
        boolean pullSuccess = feedService.updateFeedByPull(currentUser);
        FeedResponseDto feedResponse = FeedResponseDto.of(feedService.getFeed(currentUser));
        if (!pullSuccess) {
            feedResponse.updateFailPullResult();
        }
        return getSuccessResponse(feedResponse);
    }

    @GetMapping("/subscribing")
    public ResponseEntity<List<SubscribingFeedResponseDto>> getSubscribingFeed() {
        List<SubscribingFeedResponseDto> recentSubscribing = feedService.getSubscribeFeed(SecurityUtil.getCurrentUserId());
        return getSuccessResponse(recentSubscribing);
    }

    @Deprecated
    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> createFeedDocument(@Validated @RequestBody CreateFeedDocumentRequestDto dto) {
//        feedService.createFeedDocument(dto);
        return getSuccessResponse();
    }

    @Deprecated
    @NonAuthorize
    @PostMapping("/update/feed")
    public void updateSubscribeFeed(@RequestBody PostResponseDto dto) {
        feedService.updateFeedByPush(dto);
    }

    @Deprecated
    @NonAuthorize
    @PostMapping("/update/list")
    public void updateFollowingList(@RequestBody UpdateListRequestDto dto) {
        feedService.updateList(dto);
    }

    @Operation(summary = "document 상태 체크용")
    @NonAuthorize
    @GetMapping("/all")
    public ResponseEntity<List<FeedDocument>> getAllFeed() {
        List<FeedDocument> list = feedService.getAllFeed();
        return getSuccessResponse(list);
    }

}