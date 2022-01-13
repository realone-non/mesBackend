package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 17-1. 설비점검 실적 등록
public interface EquipmentCheckDetailRepositoryCustom {
    // 설비 리스트 조회
    // 검색조건: 설비유형, 점검유형(보류), 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    List<EquipmentCheckResponse> findEquipmentChecksResponseByCondition(Long workLineId, LocalDate fromDate, LocalDate toDate);
    // 설비 단일 조회
    Optional<EquipmentCheckResponse> findEquipmentChecksResponseByEquipmentId(Long equipmentId);
    // ================================================ 설비점검 실적 상세 정보 ================================================
    // 상세정보 전체 조회
    List<EquipmentCheckDetailResponse> findEquipmentCheckDetailResponseByEquipmentId(Long equipmentId);
    // 상세정보 단일 조회
    Optional<EquipmentCheckDetailResponse> findEquipmentCheckDetailResponseByEquipmentIdAndEquipmentDetailId(Long equipmentId, Long equipmentCheckDetailId);
}
