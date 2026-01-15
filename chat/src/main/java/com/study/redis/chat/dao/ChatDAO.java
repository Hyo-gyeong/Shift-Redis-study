package com.study.redis.chat.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	
	public ChatResponseDTO saveChat(ChatRequestDTO dto) {
	    ChatEntity entity = new ChatEntity();
	    entity.setDateTime(LocalDateTime.now());
	    entity.setMessage(dto.getMessage());
	    entity.setUserId(dto.getUserId());
	    
	    // 저장된 엔티티 반환
	    ChatEntity savedEntity = chatRepo.save(entity);
	    
	    // ChatResponseDTO로 변환해서 반환
	    ChatResponseDTO response = new ChatResponseDTO();
	    response.setPk(savedEntity.getPk());
	    response.setUserId(savedEntity.getUserId());
	    response.setMessage(savedEntity.getMessage());
	    response.setDateTime(savedEntity.getDateTime());
	    
	    return response;
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
