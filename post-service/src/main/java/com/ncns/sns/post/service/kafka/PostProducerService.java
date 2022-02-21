package com.ncns.sns.post.service.kafka;

import com.ncns.sns.post.dto.request.HashtagConsumerRequestDto;
import com.ncns.sns.post.dto.request.UpdateHashtagConsumerRequestDto;
import com.ncns.sns.post.dto.response.PostResponseDto;
import dev.ncns.sns.common.util.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUpdateFeedRequest(PostResponseDto postResponseDto) {
        log.info("[Kafka producer] >> update subscriber feed");
        kafkaTemplate.send(Topic.FEED_POST_UPDATE, postResponseDto);
    }

    public void sendCreatePostRequest(HashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka producer] >> create post document");
        kafkaTemplate.send(Topic.SEARCH_POST_CREATE, hashtagConsumerRequest);
    }

    public void sendUpdatePostRequest(UpdateHashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka producer] >> update post document");
        System.out.println(hashtagConsumerRequest.getHashtags());
        kafkaTemplate.send(Topic.SEARCH_POST_UPDATE, hashtagConsumerRequest);
    }

    public void sendDeletePostRequest(HashtagConsumerRequestDto hashtagConsumerRequest) {
        log.info("[Kafka producer] >> delete post document");
        kafkaTemplate.send(Topic.SEARCH_POST_DELETE, hashtagConsumerRequest);
    }

}
