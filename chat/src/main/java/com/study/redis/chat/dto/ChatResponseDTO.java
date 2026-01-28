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
public class ChatResponseDTO {
	private long pk;
	private String message;
	private LocalDateTime dateTime;
	private long userId;
	
	public static ChatResponseDTO toDTO(ChatEntity entity) {
        return ChatResponseDTO.builder()
            .pk(entity.getPk())
            .message(entity.getMessage())
            .dateTime(entity.getDateTime())
            .userId(entity.getUserId())
            .build();
    }
	
	public ChatEntity toEntity() {
        ChatEntity entity = new ChatEntity();
        entity.setPk(this.pk);
        entity.setMessage(this.message);
        entity.setUserId(this.userId);
        entity.setDateTime(this.dateTime);
        return entity;
    }
	
	public static ChatResponseDTO from(ChatRequestDTO dto) {
		return ChatResponseDTO.builder()
				.message(dto.getMessage())
				.dateTime(LocalDateTime.now())
				.userId(dto.getUserId())
				.build();
	}
}
