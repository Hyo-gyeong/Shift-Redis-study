package com.study.redis.session.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.study.redis.session.SessionStore;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    // 테스트용 아이디, 비번 상수로 정의 (DB 대신)
    private static final String USER_ID = "test";
    private static final String PASSWORD = "1234";
    
    public boolean authenticate(String userId, String password) {
        return USER_ID.equals(userId) && PASSWORD.equals(password);
    }
    
    public void saveSessionAndCookie(HttpServletResponse res, SessionStore sessionStore, String userId, String pw) {
    	// 세션 ID 생성
        String sessionId = UUID.randomUUID().toString();
        // 세션 저장소에 세션 저장
        sessionStore.save(sessionId, userId, pw);
        
        // 클라이언트에 전달할 쿠키 생성
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        // JS에서 접근 불가하도록 설정 (보안)
        cookie.setHttpOnly(true);
        // 모든 경로에서 쿠키 사용 가능
        cookie.setPath("/");
        // 응답에 쿠키 추가
        res.addCookie(cookie);   
    }
    
    public void removeCookie(HttpServletResponse res) {
    	Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 삭제
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }
    
    // 쿠키에서 JSESSIONID 추출
    public Optional<String> getSessionId(HttpServletRequest req) {
        // 쿠키가 없으면 empty 반환
        if (req.getCookies() == null) return Optional.empty();

        // 쿠키 중 JSESSIONID 찾기
        return Arrays.stream(req.getCookies())
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
