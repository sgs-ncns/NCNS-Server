package dev.ncns.sns.user.service.kafka;


import dev.ncns.sns.user.dto.request.UpdateListRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreateDocumentRequest(Long userId) {
        log.info("[Kafka producer] >> create user document");
        this.kafkaTemplate.send("NCNS-USER", userId);
    }

    public void sendUpdateListRequest(UpdateListRequestDto dto) {
        log.info("[Kafka producer] >> update follow/subscribe list");
        this.kafkaTemplate.send("NCNS-LIST", dto);
    }
}