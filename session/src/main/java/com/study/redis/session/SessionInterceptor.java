package com.study.redis.session;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// Spring Security 없이 인증 구현할 때 자주 사용하는 요청 가로채기, 모든 API에서 중복 인증 코드 제거
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionStore sessionStore;

    // 컨트롤러 실행 전 호출
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        // 인증 API는 검사 제외
        if (req.getRequestURI().startsWith("/auth")) {
            return true;
        }
        // 쿠키가 없으면 인증 실패
        if (req.getCookies() == null) {
            res.setStatus(401);
            return false;
        }
        // 모든 쿠키 탐색
        for (Cookie c : req.getCookies()) {
            // 세션 쿠키 발견
            if ("JSESSIONID".equals(c.getName())) {
                SessionUser user = sessionStore.get(c.getValue());
                // 유효한 세션이면
                if (user != null) {
                    // 요청 객체에 사용자 정보 저장
                    req.setAttribute("userId", user.getUserId());
                    // 컨트롤러 실행 허용
                    return true;
                }
            }
        }

        // 세션이 없거나 만료됨
        res.setStatus(401);
        return false;
    }
}
