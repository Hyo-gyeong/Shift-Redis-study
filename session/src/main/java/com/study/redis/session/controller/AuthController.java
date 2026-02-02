package com.study.redis.session.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.redis.session.SessionStore;
import com.study.redis.session.SessionUser;
import com.study.redis.session.dto.LoginRequestDTO;
import com.study.redis.session.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final SessionStore sessionStore;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req, // JSON 요청 바디 매핑
					               HttpServletResponse res) {   // 쿠키 설정을 위한 response
        // 아이디/비밀번호 검증
        if (!authService.authenticate(req.getUserId(), req.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        authService.saveSessionAndCookie(res, sessionStore, req.getUserId(), req.getPassword());
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        // 요청에서 세션 ID 추출
    	authService.getSessionId(req).ifPresent(sessionStore::remove);
        
        authService.removeCookie(res);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest req) {

        Optional<String> sessionIdOpt = authService.getSessionId(req); // 함수로 따로 정의

        if (sessionIdOpt.isEmpty()) {
            return ResponseEntity.status(401).body("NOT_LOGIN");
        }

        String sessionId = sessionIdOpt.get();
        // SessionUser 객체로 받기
        SessionUser sessionUser = sessionStore.get(sessionId);

        if (sessionUser == null) {
            return ResponseEntity.status(401).body("NOT_LOGIN");
        }

        // id 꺼내서 반환
        return ResponseEntity.ok(sessionUser.getUserId());
    }
    
}
