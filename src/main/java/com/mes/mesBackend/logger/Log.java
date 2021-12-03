package com.mes.mesBackend.logger;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "log")
public class Log {
    private String level;
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String requester;
    private String userCode;
    private Long resourceId;
    private String message;

    public Log(String level, String requester, String userCode, Long resourceId, String message) {
        this.level = level;
        this.timeStamp = LocalDateTime.now();
        this.requester = requester;
        this.userCode = userCode;
        this.resourceId = resourceId;
        this.message = message;
    }
}
