package dev.ncns.sns.post.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.post.dto.kafka.PostHashtagRequestDto;
import dev.ncns.sns.post.dto.kafka.UpdatePostHashtagRequestDto;
import dev.ncns.sns.post.dto.response.LikeResponseDto;
import dev.ncns.sns.post.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUpdateFeedRequest(PostResponseDto postResponse) {
        log.info("[Kafka producer] >> update subscriber feed");
        kafkaTemplate.send(Topic.FEED_POST_UPDATE, postResponse);
    }

    public void sendUpdateLikeRequest(LikeResponseDto likeResponse) {
        log.info("[Kafka producer] >> update like status");
        kafkaTemplate.send(Topic.FEED_POST_LIKE_UPDATE, likeResponse);
    }

    public void sendCreatePostRequest(PostHashtagRequestDto postHashtagRequest) {
        log.info("[Kafka producer] >> create post hashtag document");
        kafkaTemplate.send(Topic.SEARCH_POST_CREATE, postHashtagRequest);
    }

    public void sendUpdatePostRequest(UpdatePostHashtagRequestDto updatePostHashtagRequest) {
        log.info("[Kafka producer] >> update post hashtag document");
        kafkaTemplate.send(Topic.SEARCH_POST_UPDATE, updatePostHashtagRequest);
    }

    public void sendDeletePostRequest(PostHashtagRequestDto postHashtagRequest) {
        log.info("[Kafka producer] >> delete post hashtag document");
        kafkaTemplate.send(Topic.SEARCH_POST_DELETE, postHashtagRequest);
    }

}
