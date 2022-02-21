package dev.ncns.sns.search.service.kafka;

import dev.ncns.sns.common.util.Topic;
import dev.ncns.sns.search.dto.kafka.UserConsumerRequestDto;
import dev.ncns.sns.search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserConsumerService {

    private static final String GROUP_ID = "group-id-ncns";

    private final UserService userService;

    @KafkaListener(topics = Topic.SEARCH_USER_CREATE, groupId = GROUP_ID)
    public void consumeUserCreate(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka consumer] >> create user document");
        userService.createUser(userConsumerRequest.convertCreateUser());
    }

    @KafkaListener(topics = Topic.SEARCH_USER_UPDATE, groupId = GROUP_ID)
    public void consumeUserUpdate(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka consumer] >> update user document");
        userService.updateUser(userConsumerRequest.convertUpdateUser());
    }

    @KafkaListener(topics = Topic.SEARCH_USER_DELETE, groupId = GROUP_ID)
    public void consumeUserDelete(Long userId) {
        log.info("[Kafka consumer] >> delete user document");
        userService.deleteUser(userId);
    }

}
