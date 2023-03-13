package com.mes.mesBackend.logger;

import com.mes.mesBackend.entity.BaseTimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogSenderScheduled extends BaseTimeEntity {
    private final LogSender logSender;

//    @Scheduled(cron = "*/30 * * * *")   // 30분 마다 실행
//    public void sendLog() {
//
//    }
}
