package dev.ncns.sns.search.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.search.dto.kafka.PostHashtagRequestDto;
import dev.ncns.sns.search.dto.kafka.UpdatePostHashtagRequestDto;
import dev.ncns.sns.search.service.HashtagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagConsumerService {

    private static final String GROUP_ID = "group-id-ncns";

    private final HashtagService hashtagService;

    @KafkaListener(topics = Topic.SEARCH_POST_CREATE, groupId = GROUP_ID)
    public void consumePostCreate(PostHashtagRequestDto postHashtagRequest) {
        log.info("[Kafka consumer] >> create post document");
        postHashtagRequest.convertCreateHashtags().forEach(hashtagService::createHashtag);
    }

    @KafkaListener(topics = Topic.SEARCH_POST_UPDATE, groupId = GROUP_ID)
    public void consumePostUpdate(UpdatePostHashtagRequestDto postHashtagRequest) {
        log.info("[Kafka consumer] >> update post document");
        postHashtagRequest.convertUpdateHashtags().forEach(hashtagService::updateHashtag);
    }

    @KafkaListener(topics = Topic.SEARCH_POST_DELETE, groupId = GROUP_ID)
    public void consumePostDelete(PostHashtagRequestDto postHashtagRequest) {
        log.info("[Kafka consumer] >> delete post document");
        postHashtagRequest.convertUpdateHashtags(false).forEach(hashtagService::updateHashtag);
    }

}
