package dev.ncns.sns.user.service.kafka;

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

    public void sendCreateDocumentRequest(UserConsumerRequestDto userConsumerRequest) {
        log.info("[Kafka producer] >> create user document");
        this.kafkaTemplate.send("NCNS-FEED-USER-CREATE", userConsumerRequest.getUserId());
        this.kafkaTemplate.send("NCNS-SEARCH-USER-CREATE", userConsumerRequest);
    }

    public void sendUpdateListRequest(UpdateListRequestDto dto) {
        log.info("[Kafka producer] >> update follow/subscribe list");
        this.kafkaTemplate.send("NCNS-LIST", dto);
    }

    public void sendDeleteDocumentRequest(Long userId) {
        log.info("[Kafka producer] >> delete user document");
//        this.kafkaTemplate.send("NCNS-FEED-USER-DELETE", userId);
        this.kafkaTemplate.send("NCNS-SEARCH-USER-DELETE", userId);
    }

}
