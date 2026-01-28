package com.study.redis.chat.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.study.redis.chat.dao.ChatDAO;
import com.study.redis.chat.dto.ChatResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatFlushService {

    private final RedisTemplate<String, ChatResponseDTO> redisTemplate;
    private final ChatDAO chatDAO;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void flushOnce() {

        List<ChatResponseDTO> pending =
            redisTemplate.opsForList().range("chat:pending", 0, -1);

        if (pending == null || pending.isEmpty()) {
        	log.info("pending chat is empty");
            return;
        }

        try {
            // 1. DB batch insert
            chatDAO.saveAll(pending);

            // 2. Redis에서 제거
            redisTemplate.delete("chat:pending");

            // 3. 확정 채팅 리스트에 추가 (읽기용)
            redisTemplate.opsForList()
                .rightPushAll("chat:messages", pending);
            
            log.info("flushed {} chats", pending.size());
        } catch (Exception e) {
            log.error("Chat flush failed", e);
            // ❗ Redis는 유지 → 다음 스케줄에 재시도
        }
    }
}
