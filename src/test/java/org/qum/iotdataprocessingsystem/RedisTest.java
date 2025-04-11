package org.qum.iotdataprocessingsystem;

import org.junit.jupiter.api.Test;
import org.qum.iotdataprocessingsystem.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() {
        redisService.setValue("name", "Alice");
        Object value = redisService.getValue("name");
        System.out.println("Value from Redis: " + value); // 输出 "Alice"
    }
}