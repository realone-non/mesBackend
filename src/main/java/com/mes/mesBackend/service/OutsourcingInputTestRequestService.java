package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.InputTestRequestRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 15-1. 외주수입검사의뢰 등록
public interface OutsourcingInputTestRequestService {
    // 외주수입검사의뢰 생성
    InputTestRequestResponse createOutsourcingInputTestRequest(InputTestRequestRequest inputTestRequestRequest) throws BadRequestException, NotFoundException;
    // 외주수입검사의뢰 리스트 검색 조회
    // 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    List<InputTestRequestResponse> getOutsourcingInputTestRequests(
            Long warehouseId,
            Long lotTypeId,
            String itemNoAndName,
            TestType testType,
            Long itemGroupId,
            TestType requestType,
            LocalDate fromDate,
            LocalDate toDate
    );
    // 외주수입검사의뢰 수정
    InputTestRequestResponse updateOutsourcingInputTestRequest(Long id, InputTestRequestRequest inputTestRequestRequest) throws BadRequestException, NotFoundException;
    // 외주수입검사의뢰 삭제
    void deleteOutsourcingInputTestRequest(Long id);
}
