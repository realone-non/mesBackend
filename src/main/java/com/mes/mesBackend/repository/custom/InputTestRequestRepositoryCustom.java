package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InspectionType;
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
            InspectionType inspectionType,
            Long itemGroupId,
            TestType testType,
            LocalDate fromDate,
            LocalDate toDate,
            InputTestDivision inputTestDivision
    );
    // LOT Master 의 생성수량
    Integer findLotMasterCreataeAmountByLotMasterId(Long lotMasterId);
    // LOT Master 의 재고수량
    Integer findLotMasterStockAmountByLotMasterId(Long lotMasterId);
    // 검사요청상태값 별 검사요청 조회
    Optional<InputTestRequest> findByIdAndInputTestDivisionAndDeleteYnFalse(Long inputTestRequestId, InputTestDivision inputTestDivision);
    // 해당 lotMasterId 가 검사를 완료 했는지 여부
    boolean findInputTestYnByLotMasterId(Long lotMasterId);
    // 검사의뢰 가능한 품목정보 조회(구매입고)
    List<ItemResponse.noAndName> findPartInputTestRequestPossibleItems();
    // 검사의뢰 가능한 품목정보 조회(외주입고)
    List<ItemResponse.noAndName> findOutsourcingInputTestRequestPossibleItems();
    // 검사의뢰 가능한 품목정보 조회(제품검사)
    List<ItemResponse.noAndName> findProductInputTestRequestPossibleItems();
    // 검사의뢰 가능한 lotMaster 조회(구매입고)
    List<InputTestRequestResponse> findPartInputTestRequestPossibleLotMasters(Long itemId);
    // 검사의뢰 가능한 lotMatser 조회(외주입고)
    List<InputTestRequestResponse> findOutSourcingInputTestRequestPossibleLotMasters(Long itemId);
    // 검사의뢰 가능한 lotMaster 조회(제품검사)
    List<InputTestRequestResponse> findProductInputTestRequestPossibleLotMasters(Long itemId);
}
