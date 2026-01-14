package com.study.redis.chat.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.study.redis.chat.dto.ChatRequestDTO;
import com.study.redis.chat.dto.ChatResponseDTO;
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
	
	public List<ChatResponseDTO> getAllChat(){
		List<ChatResponseDTO> dtoList = new ArrayList<ChatResponseDTO>();
		List<ChatEntity> entityList = chatRepo.findAll();
		for (ChatEntity e : entityList) {
			ChatResponseDTO dto = ChatResponseDTO.builder()
									.pk(e.getPk())
									.message(e.getMessage())
									.dateTime(e.getDateTime())
									.userId(e.getUserId())
									.build();
			dtoList.add(dto);
		}
		return dtoList;
	}
}
