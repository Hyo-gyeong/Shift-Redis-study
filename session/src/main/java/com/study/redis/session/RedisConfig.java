package com.study.redis.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, SessionUser> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, SessionUser> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // key: String
        template.setKeySerializer(new StringRedisSerializer());

        // value: JSON
        template.setValueSerializer(RedisSerializer.json());
        
        template.afterPropertiesSet();

        return template;
    }
}
