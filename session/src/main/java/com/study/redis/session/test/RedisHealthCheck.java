package com.study.redis.session.test;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisHealthCheck {

    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void check() {
        redisTemplate.opsForValue().set("health", "ok");
        System.out.println("REDIS HEALTH = " +
            redisTemplate.opsForValue().get("health"));
    }
}
