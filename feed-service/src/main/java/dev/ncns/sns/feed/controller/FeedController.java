package dev.ncns.sns.feed.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.FollowUpdateRequestDto;
import dev.ncns.sns.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @NonAuthorize
    @PostMapping("/follow")
    public void updateFollowingList(@RequestBody FollowUpdateRequestDto dto) {
        feedService.updateFollowings(dto);
    }

}