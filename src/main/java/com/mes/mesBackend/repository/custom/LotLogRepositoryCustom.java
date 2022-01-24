package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.LotLogResponse;
import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.util.List;
import java.util.Optional;

public interface LotLogRepositoryCustom {
    // 작업지시 id 로 createdDate 가 제일 최근인 workProcess 를 가져옴
    Optional<LotLog> findWorkProcessNameByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 모든 불량수량 가져옴
    List<Integer> findBadItemAmountByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 모든 생성수량 가져옴
    List<Integer> findCreatedAmountByWorkOrderId(Long workOrderId);
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
}
