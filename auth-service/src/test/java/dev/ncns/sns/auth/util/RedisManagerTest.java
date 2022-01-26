package dev.ncns.sns.auth.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisManagerTest {

    private String key;

    private String value;

    private long timeout;

    private RedisManager redisManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    private void beforeTest() {
        key = "TestData";
        value = "This is test data!";
        timeout = 1000L;
        redisManager = new RedisManager(redisTemplate);
    }

    @AfterEach
    private void afterTest() {
        redisManager.deleteValue(key);

        String result = redisManager.getValue(key);
        System.out.println("Delete Result : " + key + " - " + result);
    }

    @Test
    void redisTest() {
        redisManager.setValue(key, value, timeout);

        String result = redisManager.getValue(key);
        System.out.println("Redis : " + key + " - " + result);
    }

}
