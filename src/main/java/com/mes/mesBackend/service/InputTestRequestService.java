package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.InputTestRequestCreateRequest;
import com.mes.mesBackend.dto.request.InputTestRequestUpdateRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 16-1. 제품검사의뢰 등록
// 15-1. 외주수입검사의뢰 등록
// 14-1. 부품수입검사의뢰 등록
public interface InputTestRequestService {
    // 검사의뢰 생성
    InputTestRequestResponse createInputTestRequest(
            InputTestRequestCreateRequest inputTestRequestRequest,
            InputTestDivision inputTestDivision
    ) throws BadRequestException, NotFoundException;
    // 검사의뢰 리스트 검색 조회
    // 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    List<InputTestRequestResponse> getInputTestRequests(
            Long warehouseId,
            Long lotTypeId,
            String itemNoAndName,
            TestType testType,
            Long itemGroupId,
            TestType requestType,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    ) throws NotFoundException;
    // 검사의뢰 단일 조회 및 예외
    InputTestRequestResponse getInputTestRequestResponse(Long id, InputTestDivision inputTestDivision) throws NotFoundException;
    // 검사의뢰 수정
    InputTestRequestResponse updateInputTestRequest(
            Long id,
            InputTestRequestUpdateRequest inputTestRequestUpdateRequest,
            InputTestDivision inputTestDivision
    ) throws BadRequestException, NotFoundException;
    // 검사의뢰 삭제
    void deleteInputTestRequest(Long id, InputTestDivision inputTestDivision) throws NotFoundException, BadRequestException;
    // 검사의뢰 단일 조회 및 예외
    InputTestRequest getInputTestRequestOrThrow(Long id, InputTestDivision inputTestDivision) throws NotFoundException;
}
