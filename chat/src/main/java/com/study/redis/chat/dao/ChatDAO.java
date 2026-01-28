package com.study.redis.chat.dao;

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

    public ChatResponseDTO save(ChatRequestDTO dto) {
        ChatEntity saved = chatRepo.save(dto.toEntity());
        return ChatResponseDTO.toDTO(saved);
    }

    public List<ChatResponseDTO> saveAll(List<ChatResponseDTO> dtoList) {
        List<ChatEntity> entities = dtoList.stream()
            .map(ChatResponseDTO::toEntity)
            .toList();

        return chatRepo.saveAll(entities).stream()
            .map(ChatResponseDTO::toDTO)
            .toList();
    }

    public List<ChatResponseDTO> getAllChat() {
        return chatRepo.findAllByOrderByPkAsc().stream()
            .map(ChatResponseDTO::toDTO)
            .toList();
    }
}
