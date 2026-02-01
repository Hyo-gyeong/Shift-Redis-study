package com.study.redis.session;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // 테스트용 아이디, 비번 상수로 정의 (DB 대신)
    private static final String USER_ID = "test";
    private static final String PASSWORD = "1234";

    public boolean authenticate(String userId, String password) {
        return USER_ID.equals(userId) && PASSWORD.equals(password);
    }
}
