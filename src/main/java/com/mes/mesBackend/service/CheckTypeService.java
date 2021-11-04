package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CheckTypeRequest;
import com.mes.mesBackend.dto.response.CheckTypeResponse;
import com.mes.mesBackend.entity.CheckType;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface CheckTypeService {
    // 점검유형 생성
    CheckTypeResponse createCheckType(CheckTypeRequest checkTypeRequest);
    // 점검유형 단일조회
    CheckTypeResponse getCheckType(Long id) throws NotFoundException;
    // 점검유형 전체 조회
    List<CheckTypeResponse> getCheckTypes();
    // 점검유형 수정 api
    CheckTypeResponse updateCheckType(Long id, CheckTypeRequest checkTypeRequest) throws NotFoundException;
    // 점검유형 삭제 api
    void deleteCheckType(Long id) throws NotFoundException;
    // 점검유형 조회 및 예외
    CheckType getCheckTypeOrThrow(Long id) throws NotFoundException;
}
