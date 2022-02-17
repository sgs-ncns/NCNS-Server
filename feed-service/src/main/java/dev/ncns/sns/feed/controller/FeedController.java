package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.domain.FeedRepository;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.FeedResponseDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
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
    private final FeedRepository repository;

    @GetMapping
    public ResponseEntity<FeedResponseDto> getFeed() {
        Long currentUser = SecurityUtil.getCurrentUserId();
        boolean pullSuccess = feedService.updateFeedByPull(currentUser);
        FeedResponseDto feedResponse = FeedResponseDto
                .of(feedService.getFollowingFeeds(currentUser),feedService.getSubscribingFeeds(currentUser));
        if (!pullSuccess) {
            feedResponse.updateFailPullResult();
        }
        return getSuccessResponse(feedResponse);
    }

    @GetMapping("/follow")
    public ResponseEntity<List<Feed>> getFollowingFeed() {
        List<Feed> followingFeed = feedService.getFollowingFeeds(SecurityUtil.getCurrentUserId());
        return getSuccessResponse(followingFeed);
    }

    @GetMapping("/subscribe")
    public ResponseEntity<List<Feed>> getSubscribingFeed() {
        List<Feed> subscribingFeed = feedService.getSubscribingFeeds(SecurityUtil.getCurrentUserId());
        return getSuccessResponse(subscribingFeed);
    }

    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> createFeedDocument(@Validated @RequestBody CreateFeedDocumentRequestDto dto) {
        feedService.createFeedDocument(dto);
        return getSuccessResponse();
    }

    @NonAuthorize
    @PostMapping("/update/feed")
    public void updateSubscribeFeed(@RequestBody PostResponseDto dto) {
        feedService.updateFeedByPush(dto);
    }

    @NonAuthorize
    @PostMapping("/update/list")
    public void updateFollowingList(@RequestBody UpdateListRequestDto dto) {
        feedService.updateList(dto);
    }

    @Operation(summary = "document 상태 체크용")
    @NonAuthorize
    @GetMapping("/all")
    public ResponseEntity<List<FeedDocument>> getAllFeed() {
        List<FeedDocument> list = repository.findAll();
        return getSuccessResponse(list);
    }

}