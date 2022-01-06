package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.InputTestRequestRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 14-1. 검사의뢰 등록
public interface InputTestRequestService {
    // 검사의뢰 생성
    InputTestRequestResponse createInputTestRequest(InputTestRequestRequest inputTestRequestRequest) throws NotFoundException, BadRequestException;
    // 검사의뢰 리스트 검색 조회, 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    List<InputTestRequestResponse> getInputTestRequests(
            Long warehouseId,
            Long lotTypeId,
            String itemNoAndName,
            TestType testType,
            Long itemGroupId,
            TestType requestType,
            LocalDate fromDate,
            LocalDate toDate
    );
    // 검사의뢰 단일 조회
    InputTestRequestResponse getInputTestRequestResponse(Long id) throws NotFoundException;
    // 검사의뢰 수정
    InputTestRequestResponse updateInputTestRequest(Long id, InputTestRequestRequest inputTestRequestRequest) throws NotFoundException, BadRequestException;
    // 검사의뢰 삭제
    void deleteInputTestRequest(Long id) throws NotFoundException;
    // 검사의뢰 단일 조회 및 예외
    InputTestRequest getInputTestRequestOrThrow(Long id) throws NotFoundException;
}
