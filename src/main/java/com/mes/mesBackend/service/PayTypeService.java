package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PayTypeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface PayTypeService {
    // 결제조건 생성
    PayTypeResponse createPayType(String payTypeRequest) throws BadRequestException, NotFoundException;
    // 결제조건 단일 조회
    PayTypeResponse getPayType(Long id) throws NotFoundException;
    // 결제조건 전체 조회
    List<PayTypeResponse> getPayTypes();
    // 결제조건 수정
    PayTypeResponse updatePayType(Long id, String payTypeRequest) throws NotFoundException, BadRequestException;
    // 결제조건 삭제
    void deletePayType(Long id) throws NotFoundException;
}
