package com.study.redis.session;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SessionStore {
    // 세션ID를 key로, 사용자 정보를 value로 저장하는 Map
    // ConcurrentHashMap: 멀티스레드 환경에서 안전
    private final Map<String, SessionUser> store = new ConcurrentHashMap<>();

    public void save(String sessionId, String userId) {
        // 세션 만료 시간을 현재 시간 + 30분으로 설정
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);
        store.put(sessionId, new SessionUser(userId, expiredAt));
    }

    public SessionUser get(String sessionId) {
        SessionUser user = store.get(sessionId);
        // 세션이 없거나 만료
        if (user == null || user.getExpiredAt().isBefore(LocalDateTime.now())) {
            store.remove(sessionId);
            return null;
        }
        return user;
    }

    // 로그아웃 시 세션 제거
    public void remove(String sessionId) {
        store.remove(sessionId);
    }
}
