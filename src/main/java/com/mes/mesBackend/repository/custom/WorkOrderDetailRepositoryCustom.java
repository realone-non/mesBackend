package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ProductionPlanResponse;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkOrderDetailRepositoryCustom {
    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 작업공정 id, 착수예정일 fromDate~endDate, 지시상태
    List<WorkOrderProduceOrderResponse> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            InstructionStatus instructionStatus
    );

    // 생산계획 수립 조회
    // 생산계획 수립 전체 조회, 검색조건: 작업라인, 작업예정일
    List<ProductionPlanResponse> findAllProductionPlanByCondition(
            Long workLineId,
            LocalDate fromDate,
            LocalDate toDate
    );

    // 생산계획 수립 단일 조회
    Optional<ProductionPlanResponse> findProductionPlanByIdAndDeleteYnFalse(Long id);
}
