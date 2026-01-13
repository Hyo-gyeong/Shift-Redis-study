package com.study.redis.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.redis.chat.dto.ChatRequestDTO;
import com.study.redis.chat.dto.ChatResponseDTO;

@RestController
public class ChatController {
	
	@GetMapping("api/chat")
	public List<ChatResponseDTO> getChatList(){
		return null;
	}
	
	@PostMapping("api/chat/send")
	public void sendChat(@RequestBody ChatRequestDTO dto) {
		return;
	}

}
