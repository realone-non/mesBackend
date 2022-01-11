package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.EquipmentBreakdownFileResponse;
import com.mes.mesBackend.dto.response.EquipmentBreakdownResponse;

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
}
