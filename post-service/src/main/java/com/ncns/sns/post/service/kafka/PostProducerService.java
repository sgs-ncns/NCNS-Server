package com.ncns.sns.post.service.kafka;

import com.ncns.sns.post.dto.response.PostResponseDto;
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
        this.kafkaTemplate.send("NCNS-POST", postResponseDto);
    }

}