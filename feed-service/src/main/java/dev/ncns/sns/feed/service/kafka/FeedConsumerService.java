package dev.ncns.sns.feed.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.feed.dto.request.UpdateListRequestDto;
import dev.ncns.sns.feed.dto.response.LikeResponseDto;
import dev.ncns.sns.feed.dto.response.PostResponseDto;
import dev.ncns.sns.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedConsumerService {

    private static final String GROUP_ID = "group-id-ncns";

    private final FeedService feedService;

    @KafkaListener(topics = Topic.FEED_USER_CREATE, groupId = GROUP_ID)
    public void consumeUser(Long userId) {
        log.info("[Kafka consumer] >> create user document");
        feedService.createFeedDocument(userId);
    }

    @KafkaListener(topics = Topic.FEED_USER_DELETE, groupId = GROUP_ID)
    public void consumeUserDelete(Long userId) {
        log.info("[Kafka consumer] >> delete user document");
        feedService.deleteFeedDocument(userId);
    }

    @KafkaListener(topics = Topic.FEED_POST_UPDATE, groupId = GROUP_ID)
    public void consumePost(PostResponseDto postResponseDto) {
        log.info("[Kafka consumer] >> update subscriber feed");
        feedService.updateFeedByPush(postResponseDto);
    }

    @KafkaListener(topics = Topic.FEED_USER_LIST_UPDATE, groupId = GROUP_ID)
    public void consumeFollow(UpdateListRequestDto updateListRequestDto) {
        log.info("[Kafka consumer] >> update follow/subscribe list");
        feedService.updateList(updateListRequestDto);
    }

    @KafkaListener(topics = Topic.FEED_POST_LIKE_UPDATE, groupId = GROUP_ID)
    public void consumeLike(LikeResponseDto likeResponseDto) {
        log.info("[Kafka consumer] >> update liking status");
        feedService.updateLiking(likeResponseDto);
    }

}
