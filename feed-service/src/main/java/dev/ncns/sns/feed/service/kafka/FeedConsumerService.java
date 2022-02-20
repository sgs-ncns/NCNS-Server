package dev.ncns.sns.feed.service.kafka;

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
    private final FeedService feedService;

    @KafkaListener(topics = "NCNS-FEED-USER-CREATE", groupId = "group-id-ncns")
    public void consumeUser(Long userId) {
        log.info("[Kafka consumer] >> create user document");
        feedService.createFeedDocument(userId);
    }

    @KafkaListener(topics = "NCNS-POST", groupId = "group-id-ncns")
    public void consumePost(PostResponseDto postResponseDto) {
        log.info("[Kafka consumer] >> update subscriber feed");
        feedService.updateFeedByPush(postResponseDto);
    }

    @KafkaListener(topics = "NCNS-LIST", groupId = "group-id-ncns")
    public void consumeFollow(UpdateListRequestDto updateListRequestDto) {
        log.info("[Kafka consumer] >> update follow/subscribe list");
        feedService.updateList(updateListRequestDto);
    }

    @KafkaListener(topics = "NCNS-LIKE", groupId = "group-id-ncns")
    public void consumeLike(LikeResponseDto likeResponseDto) {
        log.info("[Kafka consumer] >> update liking status");
        feedService.updateLiking(likeResponseDto);
    }
}
