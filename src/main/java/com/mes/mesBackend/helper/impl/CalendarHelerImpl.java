package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.helper.CalendarHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;

import static com.mes.mesBackend.helper.Constants.FORMAT_02;

@Component
@RequiredArgsConstructor
public class CalendarHelerImpl implements CalendarHelper {
    private final Calendar calendar = Calendar.getInstance();
    private final LocalDate now = LocalDate.now();

    // 현재 달의 시작일
    // yyyy-mm-dd 형식으로 리턴
    @Override
    public LocalDate getNowMonthStartDate() {
        // 캘린더의 기준이 될 날짜 세팅(현재 년도, 현재 월, 1)
        calendar.set(now.getYear(), now.getDayOfMonth(), 1);

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(String.format(FORMAT_02, calendar.get(Calendar.MONTH) - 1));
        String startDay = String.valueOf(String.format(FORMAT_02, calendar.get(Calendar.DAY_OF_MONTH)));

        return LocalDate.parse(year + "-" + month + "-" + startDay);
    }

    // 현재 달의 종료일
    // yyyy-mm-dd 형식으로 리턴
    @Override
    public LocalDate getNowMonthEndDate() {
        // 캘린더의 기준이 될 날짜 세팅(현재 년도, 현재 월, 1)
        calendar.set(now.getYear(), now.getDayOfMonth(), 1);

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(String.format(FORMAT_02, calendar.get(Calendar.MONTH) - 1));
        String endDay = String.valueOf(String.format(FORMAT_02, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));

        return LocalDate.parse(year + "-" + month + "-" + endDay);
    }
}
