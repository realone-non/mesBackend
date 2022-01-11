package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EquipmentCheckDetailRequest;
import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 17-1. 설비점검 실적 등록
public interface EquipmentCheckService {
    // 설비 리스트 조회
    // 검색조건: 설비유형, 점검유형(보류), 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    List<EquipmentCheckResponse> getEquipmentChecks(Long workLineId, LocalDate fromDate, LocalDate toDate);
    // 설비 단일 조회
    EquipmentCheckResponse getEquipmentCheckResponse(Long equipmentId) throws NotFoundException;

    // ================================================ 설비점검 실적 상세 정보 ================================================
    // 상세정보 생성
    EquipmentCheckDetailResponse createEquipmentCheckDetail(Long equipmentId, EquipmentCheckDetailRequest equipmentCheckDetailRequest) throws NotFoundException;
    // 상세정보 전체 조회
    List<EquipmentCheckDetailResponse> getEquipmentCheckDetails(Long equipmentId);
    // 상세정보 단일 조회
    EquipmentCheckDetailResponse getEquipmentCheckDetailResponseOrThrow(Long equipmentId, Long equipmentCheckDetailId) throws NotFoundException;
    // 상세정보 수정
    EquipmentCheckDetailResponse updateEquipmentCheckDetail(Long equipmentId, Long equipmentCheckDetailId, EquipmentCheckDetailRequest equipmentCheckDetailRequest) throws NotFoundException;
    // 상제정보 삭제
    void deleteEquipmentCheckDetail(Long equipmentId, Long equipmentCheckDetailId) throws NotFoundException;
}
