package dev.ncns.sns.search.service.kafka;

import dev.ncns.sns.search.dto.request.UserConsumerRequestDto;
import dev.ncns.sns.search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserConsumerService {

    private final UserService userService;

    @KafkaListener(topics = "NCNS-SEARCH-USER-CREATE", groupId = "group-id-ncns")
    public void consumeUserCreate(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka consumer] >> create user document");
        userService.createUser(userConsumerRequest.convertCreateUser());
    }

    @KafkaListener(topics = "NCNS-SEARCH-USER-UPDATE", groupId = "group-id-ncns")
    public void consumeUserUpdate(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka consumer] >> update user document");
        userService.updateUser(userConsumerRequest.convertUpdateUser());
    }

    @KafkaListener(topics = "NCNS-SEARCH-USER-DELETE", groupId = "group-id-ncns")
    public void consumeUserDelete(Long userId) {
        log.info("[Kafka consumer] >> delete user document");
        userService.deleteUser(userId);
    }

}
