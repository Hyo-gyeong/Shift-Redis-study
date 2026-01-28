package com.study.redis.chat.test;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.study.redis.chat.dto.ChatResponseDTO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisConnectionTest {

    private final RedisTemplate<String, ChatResponseDTO> redisTemplate;

    @PostConstruct
    public void test() {
    	LocalDateTime now = LocalDateTime.now();
    	ChatResponseDTO testDTO = new ChatResponseDTO(1, "test", now, 1);
        redisTemplate.opsForValue().set("hello", testDTO);
        Object value = redisTemplate.opsForValue().get("hello");
        System.out.println("🔴 Redis test value = " + value.toString());
    }
}
