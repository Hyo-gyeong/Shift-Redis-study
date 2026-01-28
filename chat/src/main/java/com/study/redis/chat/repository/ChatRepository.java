package com.study.redis.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.redis.chat.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Long>{

	List<ChatEntity> findAllByOrderByPkAsc();
	
}