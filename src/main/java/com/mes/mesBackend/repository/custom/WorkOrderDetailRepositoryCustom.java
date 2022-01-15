package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.enumeration.OrderState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 6-3. 생산계획 수립
// 6-2. 작업지시 등록
// 8-1. 작지상태 확인
public interface WorkOrderDetailRepositoryCustom {
    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 작업공정 id, 착수예정일 fromDate~endDate, 지시상태
    List<WorkOrderProduceOrderResponse> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            OrderState orderState
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
    // 제조오더에 해당하는 작업지시의 orderState 들
    List<OrderState> findOrderStatesByProduceOrderId(Long produceOrderId);

    // ==================================== 6-2. 작업지시 등록 ====================================
    // 작업지시 리시트 조회
    List<WorkOrderResponse> findWorkOrderResponseByProduceOrderIdAndDeleteYnFalse(Long produceOrderId);
    // 작업지시 단일 조회
    Optional<WorkOrderResponse> findWorkOrderResponseByProduceOrderIdAndWorkOrderId(Long produceOrderId, Long workOrderId);

    // =============================================== 8-1. 작지상태 확인 ===============================================
    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    List<WorkOrderStateResponse> findWorkOrderStateResponsesByCondition(
            Long workProcessId,
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    );

    // 작업지시 정보 단일 조회
    Optional<WorkOrderStateResponse> findWorkOrderStateResponseById(Long id);
    // 작업지시 상태 이력 정보 조회
    WorkOrderStateDetailResponse findWorkOrderStateDetailById(Long id);

    // =============================================== 8-1. 작지상태 확인 ===============================================
    // 작업자 투입 리스트 검색 조회, 검색조건: 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    List<WorkOrderUserResponse> findWorkOrderUserResponsesByCondition(
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    );
    // 작업자 투입 단일 조회
    Optional<WorkOrderUserResponse> findWorkOrderUserResponseByIdAndDeleteYn(Long workOrderId);

    // 제조오더에 해당된 작업지시 정보의 지시수량 모두
    List<Integer> findOrderAmountsByProduceOrderId(Long produceOrderId);

    // =============================================== pop ===============================================
    // 작업지시 정보 리스트, 조건: 작업자, 작업공정
    List<PopWorkOrderResponse> findPopWorkOrderResponsesByCondition(Long workProcessId, Long userId, LocalDate fromDate, LocalDate toDate);
    // 작업지시 상세 정보
    List<PopWorkOrderDetailResponse> findPopWorkOrderDetailResponsesByItemId(Long itemId);
    // 수주수량
    Integer findContractItemAmountByWorkOrderId(Long workOrderId);

    // =============================================== 8-5. 불량등록 ===============================================
    // 작업지시 정보 리스트 조회, 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    List<BadItemWorkOrderResponse> findBadItemWorkOrderResponseByCondition(
            Long workCenterId,
            Long workLineId,
            Long itemGroupId,
            String produceOrderNo,
            String workOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndItemName
    );
}
