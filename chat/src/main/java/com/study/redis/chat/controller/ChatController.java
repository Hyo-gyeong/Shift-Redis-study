package com.study.redis.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.redis.chat.dto.ChatRequestDTO;
import com.study.redis.chat.dto.ChatResponseDTO;
import com.study.redis.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
	
	private final ChatService chatService;
	
	@GetMapping()
	public List<ChatResponseDTO> getChatList(){
		return null;
	}
	
	@MessageMapping("/send")
	public void sendChat(@Payload ChatRequestDTO dto) {
		chatService.sendAndSaveMessage(dto);
	}

}
