package com.mes.mesBackend.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Document(collation = "log")
public class Log {
    private String level;
    private String timeStamp = LocalDateTime.now().toString();
    private String message;
}
