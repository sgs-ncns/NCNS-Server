package dev.ncns.sns.auth.util;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis에 "key,value"를 생성하고, 가져오고, 삭제할 수 있도록 하는 Util입니다.
 */
@RequiredArgsConstructor
@Component
public class RedisManager {

    private final StringRedisTemplate redisTemplate;

    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (value == null) {
            throw new NotFoundException(ResponseType.AUTH_NOT_FOUND_REDIS_KEY);
        }
        return value;
    }

    public void setValue(String key, String value, long timeout) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, Duration.ofMillis(timeout));
    }

    public void deleteValue(String key) {
        if (Boolean.FALSE.equals(redisTemplate.delete(key))) {
            throw new NotFoundException(ResponseType.AUTH_NOT_FOUND_REDIS_KEY);
        }
    }

}
