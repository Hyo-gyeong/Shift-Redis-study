package com.study.redis.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatEntity {
	@Id
	@GeneratedValue(
	    strategy = GenerationType.SEQUENCE,
	    generator = "SEQ_MSG"
	)
	@SequenceGenerator(
	    name = "SEQ_MSG",
	    sequenceName = "SEQ_MSG",
	    allocationSize = 1
	)
	private long pk;
	private String message;
	private LocalDateTime  dateTime;
	private long userId;
}