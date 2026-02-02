package com.study.redis.session.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 시 클라이언트에서 전달하는 JSON을
 * Java 객체로 매핑하기 위한 DTO
 */
@Getter
@Setter
public class LoginRequestDTO {
    private String userId;
    private String password;
}
