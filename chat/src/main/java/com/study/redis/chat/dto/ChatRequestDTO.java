package com.study.redis.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRequestDTO {
	private String message;
	private long userId;
}
