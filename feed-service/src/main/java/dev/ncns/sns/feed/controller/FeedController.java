package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.service.FeedService;
import dev.ncns.sns.feed.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import dev.ncns.sns.common.annotation.NonAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/feed")
@RestController
public class FeedController extends ApiController {

    private final FeedService feedService;

    /**
     * (Default) Pull 정책 Endpoint
     * 피드 요청 시 Post 서버로 현재 피드 정보 업데이트를 요청합니다.
     * 일반 팔로잉하는 계정의 게시물은 구독에 비해 수가 많고 중요도가 떨어집니다.
     * 따라서 매번 sync 요청을 하지 않고 한번에 누적 게시글을 끌어오는 정책을 적용했습니다.
     * 새 피드 정보를 갱신 후 pagination 해서 리턴합니다.
     */
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

    /**
     * Push 정책 Endpoint (Event Sourcing)
     * Post 서버에서 게시글 등록 시 작성자를 팔로우하는 모든 유저에 대해 피드 서버에 업데이트 요청합니다.
     * 피드 서버는 해당 게시글 작성자를 구독하는 유저의 피드에 게시글을 추가합니다.
     * 구독하는 계정의 게시물은 어떤 상황에서도 최대한 온전한 동기화 상태를 유지할 수 있도록 해당 정책을 적용했습니다.
     */
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

}