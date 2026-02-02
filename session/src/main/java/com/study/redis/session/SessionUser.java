package com.study.redis.session;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // getter, setter, toString 자동 생성
@AllArgsConstructor
public class SessionUser { // 세션에 매핑된 사용자 정보
    private String userId;
    private String password;
    private LocalDateTime expiredAt;
}
