package com.study.redis.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// RedisConnectionFactory = Redis 연결 생성 공장
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.study.redis.chat.dto.ChatResponseDTO;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ChatResponseDTO> chatRedisTemplate(
            RedisConnectionFactory connectionFactory) {
    	// RedisConnectionFactory는 Redis와 통신하기 위한 연결을 필요할 때마다 만들어주는 관리자

        RedisTemplate<String, ChatResponseDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key
        template.setKeySerializer(new StringRedisSerializer());

        // Value (DTO JSON)
        // Generic / json 기반 RedisSerializer는 역직렬화 복원을 위해 타입 정보를 강제로 넣음
        template.setValueSerializer(RedisSerializer.json());

        // 필수 속성 검사
        template.afterPropertiesSet();
        
        // bean 반환, Spring Context 등록(싱글톤)
        return template;
    }
}

