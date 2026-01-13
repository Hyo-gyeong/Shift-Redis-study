package com.study.redis.chat.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Chat")
@Getter
@Setter
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
	private Date dateTime;
	private long userId;
}