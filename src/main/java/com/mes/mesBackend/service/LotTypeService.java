package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.LotTypeRequest;
import com.mes.mesBackend.dto.response.LotTypeResponse;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// Lot유형
public interface LotTypeService {
    // Lot유형 생성
    LotTypeResponse createLotType(LotTypeRequest lotTypeRequest);
    // Lot유형 단일 조회
    LotTypeResponse getLotType(Long id) throws NotFoundException;
    // Lot유형 리스트 조회
    List<LotTypeResponse> getLotTypes();
    // Lot유형 수정
    LotTypeResponse updateLotType(Long id, LotTypeRequest lotTypeRequest) throws NotFoundException;
    // Lot유형 삭제
    void deleteLotType(Long id) throws NotFoundException;
    // Lot유형 조회 및 예외
    LotType getLotTypeOrThrow(Long id) throws NotFoundException;
}
