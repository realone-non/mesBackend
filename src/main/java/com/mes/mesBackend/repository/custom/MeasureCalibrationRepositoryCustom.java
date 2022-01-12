package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.MeasureCalibrationResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 17-5. 계측기 검교정 실적 등록
public interface MeasureCalibrationRepositoryCustom {
    // 계측기 검교정 실적 단일 조회
    Optional<MeasureCalibrationResponse> findMeasureCalibrationResponseById(Long id);
    // 계측기 검교정 실적 리스트 검색 조회, 검색조건: 검정처(부서 id), 측정기유형(계측기유형), 검정기간(검교정일자) fromDate~toDate
    List<MeasureCalibrationResponse> findMeasureCalibrationResponsesByCondition(Long departmentId, Long gaugeTypeId, LocalDate fromDate, LocalDate toDate);
}
