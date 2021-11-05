package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.UseTypeRequest;
import com.mes.mesBackend.dto.response.UseTypeResponse;
import com.mes.mesBackend.entity.UseType;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 용도유형
public interface UseTypeService {
    // 용도유형 생성
    UseTypeResponse createUseType(UseTypeRequest useTypeRequest);
    // 용도유형 단일 조회
    UseTypeResponse getUseType(Long id) throws NotFoundException;
    // 용도유형 리스트 조회
    List<UseTypeResponse> getUseTypes();
    // 용도유형 수정
    UseTypeResponse updateUseType(Long id, UseTypeRequest useTypeRequest) throws NotFoundException;
    // 용도유형 삭제
    void deleteUseType(Long id) throws NotFoundException;
    // 용도유형 조회 및 예외
    UseType getUseTypeOrThrow(Long id) throws NotFoundException;
}
