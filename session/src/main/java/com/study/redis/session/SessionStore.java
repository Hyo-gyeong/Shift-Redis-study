package com.study.redis.session;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionStore {

    private final RedisTemplate<String, SessionUser> redisTemplate;

    public void save(String sessionId, String userId) {
    	int sessionDurationMinutes = 2;
        SessionUser user = new SessionUser(userId, sessionId, LocalDateTime.now().plusMinutes(sessionDurationMinutes));

        // Redis에 저장 + TTL 설정
        redisTemplate.opsForValue()
                     .set("session", user, Duration.ofMinutes(sessionDurationMinutes));
    }

    public SessionUser get(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }

    public void remove(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
