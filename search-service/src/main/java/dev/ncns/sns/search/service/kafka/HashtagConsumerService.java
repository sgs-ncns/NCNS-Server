package dev.ncns.sns.search.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.search.dto.request.HashtagConsumerRequestDto;
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
    public void consumePostCreate(HashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka consumer] >> create post document");
        hashtagConsumerRequest.convertCreateHashtags().forEach(hashtagService::createHashtag);
    }

    @KafkaListener(topics = Topic.SEARCH_POST_UPDATE, groupId = GROUP_ID)
    public void consumePostUpdate(HashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka consumer] >> update post document");
    }

    @KafkaListener(topics = Topic.SEARCH_POST_DELETE, groupId = GROUP_ID)
    public void consumePostDelete(HashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka consumer] >> delete post document");
    }

}
