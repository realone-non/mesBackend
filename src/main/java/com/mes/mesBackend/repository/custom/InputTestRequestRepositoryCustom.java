package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 14-1. 검사의뢰 등록
public interface InputTestRequestRepositoryCustom {
    // 검사의뢰등록 response 단일 조회 및 예외
    Optional<InputTestRequestResponse> findResponseByIdAndDeleteYnFalse(Long id, InputTestDivision inputTestDivision);
    // 검사의뢰 리스트 검색 조회
    // 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    List<InputTestRequestResponse> findAllByCondition(
            Long warehouseId,
            Long lotTypeId,
            String itemNoAndName,
            TestType testType,
            Long itemGroupId,
            TestType requestType,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    );
    // LOT Master 의 재고수량
    // inputTestService.createInputTest 에서 사용
    Integer findLotMasterStockAmountByLotMasterId(Long lotMasterId);
    // 검사요청상태값 별 검사요청 조회
    Optional<InputTestRequest> findByIdAndInputTestDivisionAndDeleteYnFalse(Long inputTestRequestId, InputTestDivision inputTestDivision);
    // lot id 로 생산실적의 workOrderDetailNo 조회
    Optional<String> findWorkOrderNoByLotId(Long lotMasterId);
    // 해당 lotMasterId 와 같은 검사 id 가져옴
    Optional<Long> findInputTestDetailIdByLotMasterId(Long lotMasterId);
}
