package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.LotLogResponse;
import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotLogRepositoryCustom {
    // 작업지시 id 로 createdDate 가 제일 최근인 workProcess 를 가져옴
    Optional<LotLog> findLotLogByWorkOrderIdAndWorkProcessId(Long workOrderId, Long workProcessId);
    // 작업지시에 해당하는 모든 불량수량 가져옴
    List<Integer> findBadItemAmountByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 모든 생성수량 가져옴
//    List<Integer> findCreatedAmountByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 lotMaster Id 모두 가져옴
    List<Long> findLotMasterIdByWorkOrderId(Long workOrderId);
    // Lot log 조회, 검색조건: 작업공정 id, 작업지시 id, lotMaster id
     List<LotLogResponse> findLotLogResponsesByCondition(Long workProcessId, Long workOrderId, Long lotMasterId);
     // 수주품목과 작업공정에 해당하는 작업지시 가져옴
     Optional<Long> findWorkOrderDetailIdByContractItemAndWorkProcess(Long contractItemId, Long workProcessId);
     // 작업공정 구분으로 작업공정 id 가져옴
     Optional<Long> findWorkProcessIdByWorkProcessDivision(WorkProcessDivision workProcessDivision);
     // lotMaster id 로 PACKAGING 끝난 작업지시 가져옴
    Optional<String> findWorkOrderIdByLotMasterIdAndWorkProcessDivision(Long lotMasterId, WorkProcessDivision workProcessDivision);
    // 검색조건: lotMasterId, workProcessId, 반환: LotLog
    Optional<LotLog> findByLotMasterIdAndWorkProcessId(Long lotMasterId, Long workProcessId);
    // 검색조건: workOrderDetailId, 반환: LotLog
    Optional<LotLog> findByWorkOrderDetailId(Long workOrderDetailId);
    // 조건: lorMasterId, 작업공정, 오늘
    Optional<LotLog> findByLotMasterIdAndWorkProcessDivision(Long lotMasterId, WorkProcessDivision workProcessDivision, LocalDate now);
    // 검색조건: lotMasterId, workProcessDivision, 반환: LotLog
    Optional<LotLog> findByLotMasterIdAndWorkProcessDivision(Long lotMasterId, WorkProcessDivision workProcessDivision);
}
