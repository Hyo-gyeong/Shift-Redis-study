package com.study.redis.chat.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// @Scheduled가 동작할 수 있도록 허용하는 설정 클래스
/* 이게 있어야
 * @Scheduled(fixedDelay = 5000)
public void flushToDb() {
    ... 
    이런 코드가 동작함
    아, 주기적으로 실행할 메서드가 있구나 하고 스캔 가능
 */
@EnableScheduling
@Configuration
public class SchedulerConfig {} // 스프링에게 스케줄러 기능을 켜라 라고 알려주는 스위치
