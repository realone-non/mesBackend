package com.mes.mesBackend.helper;

import java.time.LocalDate;

public interface LocalDateHelper {
    // 현재 달의 시작일
    LocalDate getNowMonthStartDate();
    // 현재 달의 종료일
    LocalDate getNowMonthEndDate();
}
