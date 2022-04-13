package com.mes.mesBackend.helper;

import java.time.LocalDate;

public interface CalendarHelper {
    // 현재 달의 시작일 yyyy-mm-dd
    LocalDate getNowMonthStartDate();
    // 현재 달의 종료일 yyyy-mm-dd
    LocalDate getNowMonthEndDate();
}
