package com.study.redis.chat.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatResponseDTO {
	private long pk;
	private String message;
	private Date dateTime;
	private long userId;
}
