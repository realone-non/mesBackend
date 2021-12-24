package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.helper.NumberAutomatic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mes.mesBackend.helper.Constants.YYMMDDHHMMSSSS;

@Service
public class NumberAutomaticImpl implements NumberAutomatic {

    /*
    * No 자동생성
    * return: yyMMddHHmmssSS
    * */
    @Override
    public String createDateTimeNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(YYMMDDHHMMSSSS));
    }
}
