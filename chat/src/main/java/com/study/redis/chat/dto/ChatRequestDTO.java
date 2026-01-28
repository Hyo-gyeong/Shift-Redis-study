package com.study.redis.chat.dto;

import java.time.LocalDateTime;

import com.study.redis.chat.entity.ChatEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {
	private String message;
	private long userId;
	
	public ChatEntity toEntity() {
        ChatEntity entity = new ChatEntity();
        entity.setMessage(this.message);
        entity.setUserId(this.userId);
        entity.setDateTime(LocalDateTime.now());
        return entity;
    }
}
