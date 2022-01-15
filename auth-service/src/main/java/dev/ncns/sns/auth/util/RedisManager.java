package dev.ncns.sns.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisManager {

    private final StringRedisTemplate redisTemplate;

    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setValue(String key, String value, long timeout) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, Duration.ofMillis(timeout));
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

}
