package dev.ncns.sns.gateway.util;

import dev.ncns.sns.gateway.domain.ResponseType;
import dev.ncns.sns.gateway.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisManager {

    private final StringRedisTemplate redisTemplate;

    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (value == null) {
            throw new NotFoundException(ResponseType.GATEWAY_NOT_FOUND_REDIS_KEY);
        }
        return value;
    }

}
