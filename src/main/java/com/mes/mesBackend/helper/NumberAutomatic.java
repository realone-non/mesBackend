package com.mes.mesBackend.helper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mes.mesBackend.helper.Constants.NUMBER_FORMAT;

@Service
public class NumberAutomatic {

    public String createDateTimeNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(NUMBER_FORMAT));
    }

}
