package com.study.redis.chat.dao;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.study.redis.chat.dto.ChatRequestDTO;
import com.study.redis.chat.entity.ChatEntity;
import com.study.redis.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatDAO {
	
	private final ChatRepository chatRepo;
	
	public void saveChat(ChatRequestDTO dto) {
		ChatEntity entity = new ChatEntity();
		entity.setDateTime(new Date());
		entity.setMessage(dto.getMessage());
		entity.setUserId(dto.getUserId());
		chatRepo.save(entity);
	}
}
