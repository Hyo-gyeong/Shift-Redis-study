package com.study.redis.session;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
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
					               HttpServletResponse response) {   // 쿠키 설정을 위한 response
        // 아이디/비밀번호 검증
        if (!authService.authenticate(req.getUserId(), req.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        // 세션 ID 생성
        String sessionId = UUID.randomUUID().toString();
        // 세션 저장소에 세션 저장
        sessionStore.save(sessionId, req.getUserId());

        // 클라이언트에 전달할 쿠키 생성
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        // JS에서 접근 불가하도록 설정 (보안)
        cookie.setHttpOnly(true);
        // 모든 경로에서 쿠키 사용 가능
        cookie.setPath("/");
        // 응답에 쿠키 추가
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 요청에서 세션 ID 추출
        getSessionId(request).ifPresent(sessionStore::remove);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {

        Optional<String> sessionIdOpt = getSessionId(request);

        if (sessionIdOpt.isEmpty()) {
            return ResponseEntity.status(401).body("NOT_LOGIN");
        }

        String sessionId = sessionIdOpt.get();

        // ⭐ SessionUser 객체로 받는다
        SessionUser sessionUser = sessionStore.get(sessionId);

        if (sessionUser == null) {
            return ResponseEntity.status(401).body("NOT_LOGIN");
        }

        // 필요한 값만 꺼내서 반환
        return ResponseEntity.ok(sessionUser.getUserId());
    }

    // 쿠키에서 JSESSIONID 추출
    private Optional<String> getSessionId(HttpServletRequest req) {
        // 쿠키가 없으면 empty 반환
        if (req.getCookies() == null) return Optional.empty();

        // 쿠키 중 JSESSIONID 찾기
        return Arrays.stream(req.getCookies())
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
