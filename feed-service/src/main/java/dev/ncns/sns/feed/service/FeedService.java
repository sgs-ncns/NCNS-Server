package dev.ncns.sns.feed.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.feed.controller.PostFeignClient;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.domain.FeedRepository;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.FeedPullRequestDto;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.FeedResponseDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final PostFeignClient postFeignClient;

    @Transactional
    public void createFeedDocument(CreateFeedDocumentRequestDto dto) {
        FeedDocument feedDocument = FeedDocument.builder().userId(dto.getUserId()).build();
        feedRepository.save(feedDocument);
    }

    public FeedResponseDto getFeeds(Long userId) {
        FeedDocument feedDocument = getFeedDocument(userId);
        return FeedResponseDto.of(feedDocument.getFollowingFeeds(), feedDocument.getSubscribingFeeds()); //TODO:: pagination
    }

    @Transactional
    public void updateFeedByPull(Long userId) {
        FeedDocument feedDocument = getFeedDocument(userId);

        List<Long> followingList = feedDocument.getFollowings();
        LocalDateTime feedLastUpdated = feedDocument.getUpdatedAt();
        FeedPullRequestDto dto = new FeedPullRequestDto(followingList, feedLastUpdated);
        // TODO: 깐부도 팔로잉에 포함이므로 중복으로 들어감 -> 처리 or 활용
        List<PostResponseDto> responseDtoList = postFeignClient.getNewFeeds(dto);
        List<Feed> newFeeds = responseDtoList.stream().map(PostResponseDto::toEntity).collect(Collectors.toList());

        feedDocument.updatefollowingFeed(newFeeds);
        feedRepository.save(feedDocument);
    }

    @Transactional
    public void updateFeedByPush(PostResponseDto dto) {
        List<Long> subscribers = getFeedDocument(dto.getUserId()).getSubscribers();
        subscribers.forEach(subscriberId -> {
            FeedDocument sub = getFeedDocument(subscriberId);
            List<Feed> temp = new ArrayList<>();
            temp.add(dto.toEntity());
            sub.updateSubscribeFeed(temp);
            feedRepository.save(sub);
        });
    }

    @Transactional
    public void updateList(UpdateListRequestDto dto) {
        FeedDocument feedDocument = new FeedDocument();
        switch (dto.getListType()) {
            case FOLLOWING:
                feedDocument = getFeedDocument(dto.getUserId());
                if (dto.getIsAdd()) {
                    feedDocument.addToList(dto.getTargetId(), dto.getListType());
                } else {
                    feedDocument.removeFromList(dto.getTargetId(), dto.getListType());
                }
                break;
            case SUBSCRIBING:
                feedDocument = getFeedDocument(dto.getTargetId());
                if (dto.getIsAdd()) {
                    feedDocument.addToList(dto.getUserId(), dto.getListType());
                } else {
                    feedDocument.removeFromList(dto.getUserId(), dto.getListType());
                }
        }
        feedRepository.save(feedDocument);
    }

    private FeedDocument getFeedDocument(Long userId) {
        return feedRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ResponseType.USER_NOT_EXIST_ID));
    }
}