package com.study.redis.chat.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.study.redis.chat.dao.ChatDAO;
import com.study.redis.chat.dto.ChatRequestDTO;
import com.study.redis.chat.dto.ChatResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatDAO chatDAO;
	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, ChatResponseDTO> redisTemplate;
	
	@Transactional
	public void sendMessage(ChatRequestDTO dto) {	
		// 1. DTO 변환
	    ChatResponseDTO message = ChatResponseDTO.from(dto);

	    // 2. Redis에 임시 저장 (Write-Back)
	    try {
	        redisTemplate.opsForList()
	            .rightPush("chat:pending", message);
	        log.info("Message is successfully sent!");
	    } catch (Exception e) {
	        log.error("Redis write failed", e);
	    }
	    
		try {
			messagingTemplate.convertAndSend("/sub/chat", message);
		} catch (MessagingException e) {
	        // 메시지 전송 실패 시 상세 로그 출력
	        log.error("Failed to send message : {}", e.getMessage(), e);
	        // 런타임 예외
	        throw new IllegalStateException("메시지 전송 중 오류가 발생했습니다.", e);

	    } catch (Exception e) {
	        log.error("Unexpected error during message broadcast: {}", e.getMessage(), e);
	        throw new RuntimeException("예상치 못한 오류가 발생했습니다.", e);
	    }
		return;
	}
	
	
	public List<ChatResponseDTO> getAllChat(){	
		try {
	        // 1. Redis 조회
	        List<ChatResponseDTO> cached =
	            redisTemplate.opsForList().range("chat:messages", 0, -1);
	        log.info("cache len {}", cached.size());
	        if (cached != null && !cached.isEmpty()) {
	            return cached.stream()
	                .map(o -> (ChatResponseDTO) o)
	                .toList();
	        }

	    } catch (Exception e) {
	        log.warn("Redis unavailable, fallback to DB", e);
	    }

		// 2. DB 조회
	    List<ChatResponseDTO> fromDb = chatDAO.getAllChat();

	    // 3. Redis 재적재
	    try {
	        if (!fromDb.isEmpty()) {
	        	for (ChatResponseDTO dto : fromDb) {
	        	    redisTemplate.opsForList().rightPush("chat:messages", dto);
	        	}
	        	log.info("redis 재적재");
	        }
	    } catch (Exception e) {
	        log.warn("Redis cache warm-up failed", e);
	    }

	    return fromDb;
	}
}
