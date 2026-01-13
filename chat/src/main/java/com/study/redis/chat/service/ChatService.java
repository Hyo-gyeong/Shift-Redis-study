package com.study.redis.chat.service;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.redis.chat.dao.ChatDAO;
import com.study.redis.chat.dto.ChatRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatDAO chatDAO;
	private final SimpMessagingTemplate messagingTemplate;
	
	@Transactional
	public void sendAndSaveMessage(ChatRequestDTO dto) {
		chatDAO.saveChat(dto);
		
		try {
			messagingTemplate.convertAndSend("/sub/messages", dto);
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
}
