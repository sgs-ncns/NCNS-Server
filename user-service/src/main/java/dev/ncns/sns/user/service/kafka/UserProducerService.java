package dev.ncns.sns.user.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.user.dto.request.UpdateListRequestDto;
import dev.ncns.sns.user.dto.request.UserConsumerRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreateUserRequest(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka producer] >> create user document");
        kafkaTemplate.send(Topic.FEED_USER_CREATE, userConsumerRequest.getUserId());
        kafkaTemplate.send(Topic.SEARCH_USER_CREATE, userConsumerRequest);
    }

    public void sendUpdateUserRequest(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka producer] >> update user document");
        kafkaTemplate.send(Topic.SEARCH_USER_UPDATE, userConsumerRequest);
    }

    public void sendUpdateListRequest(UpdateListRequestDto updateListRequest) {
        log.info("[Kafka producer] >> update follow/subscribe list");
        kafkaTemplate.send(Topic.FEED_USER_LIST_UPDATE, updateListRequest);
    }

    public void sendDeleteUserRequest(Long userId) {
        log.info("[Kafka producer] >> delete user document");
        kafkaTemplate.send(Topic.FEED_USER_DELETE, userId);
        kafkaTemplate.send(Topic.SEARCH_USER_DELETE, userId);
    }

}
