package com.mes.mesBackend.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "log")
public class Log {
    private String level;
    private String timeStamp = LocalDateTime.now().toString();
    private String requester;
    private String userCode;
    private Long resourceId;
    private String message;

//    public Log(String level, String requester, String userCode, Long resourceId, String message) {
//        this.level = level;
//        this.timeStamp = LocalDateTime.now().toString();
//        this.requester = requester;
//        this.userCode = userCode;
//        this.resourceId = resourceId;
//        this.message = message;
//    }
}
