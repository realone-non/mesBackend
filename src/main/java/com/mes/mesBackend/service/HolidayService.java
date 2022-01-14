package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.request.HolidayRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.dto.response.HolidayResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface HolidayService {
    // 휴일 생성
    HolidayResponse createHoliday(HolidayRequest Request) throws BadRequestException;
    // 휴일 단일 조회
    HolidayResponse getHoliday(Long id) throws NotFoundException;
    // 휴일 페이징 조회
    List<HolidayResponse> getHolidays();
    // 휴일 수정
    HolidayResponse updateHoliday(Long id, HolidayRequest request) throws NotFoundException, BadRequestException;
    // 휴일 삭제
    void deleteHoliday(Long id) throws NotFoundException;
}
