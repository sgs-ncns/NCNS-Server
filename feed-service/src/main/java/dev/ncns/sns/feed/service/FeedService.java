package dev.ncns.sns.feed.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.feed.controller.PostFeignClient;
import dev.ncns.sns.feed.domain.Feed;
import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.domain.FeedRepository;
import dev.ncns.sns.feed.domain.ListType;
import dev.ncns.sns.feed.dto.request.FeedPullRequestDto;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.dto.response.SubscribingFeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final PostFeignClient postFeignClient;

    private static final int PAGE_SIZE = 5;

    @Transactional
    public void createFeedDocument(Long userId) {
        FeedDocument feedDocument = FeedDocument.builder().userId(userId).build();
        feedRepository.save(feedDocument);
    }

    public List<Feed> getFeed(Long userId, int page) {
        if (page <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        List<Feed> feeds = getFeedDocument(userId).getFeeds();
        Collections.reverse(feeds);
        int start = PAGE_SIZE * (page - 1);
        int end = feeds.size() <= PAGE_SIZE * page ? feeds.size() - 1 : PAGE_SIZE * page;

        return feeds.subList(start, end);
    }

    public List<SubscribingFeedResponseDto> getSubscribeFeed(Long userId) {
        List<SubscribingFeedResponseDto> response = new ArrayList<>();

        FeedDocument feedDocument = getFeedDocument(userId);
        List<Feed> feeds = feedDocument.getFeeds();
        Collections.reverse(feeds);
        List<Long> subscribings = feedDocument.getRecentSubscribing();

        subscribings.forEach(sub -> {
            SubscribingFeedResponseDto dto = new SubscribingFeedResponseDto();
            for (Feed feed : feeds) {
                dto.setUserId(sub);
                if (sub.equals(feed.getUserId())) dto.add(feed);
                if (dto.getRecentFeeds().size() == PAGE_SIZE) break;
            }
            response.add(dto);
        });
        return response;
    }

    @Transactional
    public boolean updateFeedByPull(Long userId) {
        FeedDocument feedDocument = getFeedDocument(userId);

        List<Long> followingList = feedDocument.getFollowings();
        List<Long> subscribingList = feedDocument.getSubscribings();
        subscribingList.forEach(followingList::remove);
        LocalDateTime feedLastUpdated = feedDocument.getUpdatedAt();
        FeedPullRequestDto dto = new FeedPullRequestDto(followingList, feedLastUpdated);
        try {
            List<PostResponseDto> responseDtoList = postFeignClient.getNewFeeds(dto);
            List<Feed> newFeeds = responseDtoList.stream().map(PostResponseDto::toEntity).collect(Collectors.toList());

            feedDocument.updateFollowingFeed(newFeeds);
            feedRepository.save(feedDocument);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void updateFeedByPush(PostResponseDto dto) {
        List<Long> subscribers = getFeedDocument(dto.getUserId()).getSubscribers();
        subscribers.forEach(subscriberId -> {
            FeedDocument sub = getFeedDocument(subscriberId);
            sub.updateSubscribeFeed(dto.toEntity());
            feedRepository.save(sub);
        });
    }

    @Transactional
    public void updateList(UpdateListRequestDto dto) {
        FeedDocument userDocument = getFeedDocument(dto.getUserId());
        FeedDocument targetDocument = getFeedDocument(dto.getTargetId());
        switch (dto.getListType()) {
            case FOLLOWING:
                if (dto.getIsAdd()) {
                    userDocument.addToList(dto.getTargetId(), ListType.FOLLOWING);
                    targetDocument.addToList(dto.getUserId(), ListType.FOLLOWER);
                } else {
                    userDocument.removeFromList(dto.getTargetId(), ListType.FOLLOWING);
                    targetDocument.removeFromList(dto.getUserId(), ListType.FOLLOWER);
                }
                break;
            case SUBSCRIBING:
                if (dto.getIsAdd()) {
                    userDocument.addToList(dto.getTargetId(), ListType.SUBSCRIBING);
                    targetDocument.addToList(dto.getUserId(), ListType.SUBSCRIBER);
                } else {
                    userDocument.removeFromList(dto.getTargetId(), ListType.SUBSCRIBING);
                    targetDocument.removeFromList(dto.getUserId(), ListType.SUBSCRIBER);
                }
        }
        feedRepository.save(userDocument);
        feedRepository.save(targetDocument);
    }

    private FeedDocument getFeedDocument(Long userId) {
        return feedRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ResponseType.USER_NOT_EXIST_ID));
    }

    public List<FeedDocument> getAllFeed() {
        return feedRepository.findAll();
    }

}