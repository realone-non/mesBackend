package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.EquipmentBreakdownFileResponse;
import com.mes.mesBackend.dto.response.EquipmentBreakdownResponse;
import com.mes.mesBackend.dto.response.EquipmentRepairHistoryResponse;
import com.mes.mesBackend.dto.response.EquipmentRepairPartResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 17-2. 설비 고장 수리내역 등록
public interface EquipmentBreakdownRepositoryCustom {
    // 설비고장 리스트 검색 조회, 검색조건: 작업장 id, 설비유형, 작업기간 fromDate~toDate
    List<EquipmentBreakdownResponse> findEquipmentBreakdownResponsesByCondition(Long workCenterId, Long workLineId, LocalDate fromDate, LocalDate toDate);
    // 설비고장 단일조회
    Optional<EquipmentBreakdownResponse> findEquipmentBreakdownResponseById(Long equipmentBreakdownId);
    // 설비고장 id 로 수리전 파일들 조회
    List<EquipmentBreakdownFileResponse> findBeforeFileResponsesByEquipmentBreakdownId(Long equipmentBreakdownId);
    // 설비고장 id 로 수리후 파일들 조회
    List<EquipmentBreakdownFileResponse> findAfterFileResponsesByEquipmentBreakdownId(Long equipmentBreakdownId);
    // ============================================== 17-3. 설비 수리내역 조회 ==============================================
    // 설비 수리내역 리스트 조회, 검색조건: 작업장 id, 설비유형, 수리항목, 작업기간 fromDate~toDate
    List<EquipmentRepairHistoryResponse> findEquipmentRepairHistoryResponseByCondition(
            Long workCenterId,
            Long workLineId,
            Long repairCodeId,
            LocalDate fromDate,
            LocalDate toDate
    );
    // ============================================== 17-4. 설비 수리부품 내역 조회 ==============================================
    // 설비 수리부품 내역 조회, 검색조건: 작업장 id, 설비유형(작업라인 id), 수리항목(수리코드 id), 작업기간 fromDate~toDate
    List<EquipmentRepairPartResponse> findEquipmentRepairPartResopnsesByCondition(
            Long workCenterId,
            Long workLineId,
            Long repairCodeId,
            LocalDate fromDate,
            LocalDate toDate
    );
}
