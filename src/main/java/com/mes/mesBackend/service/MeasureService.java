package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.MeasureRequest;
import com.mes.mesBackend.dto.response.MeasureResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasureService {
    // 계측기 생성
    MeasureResponse createMeasure(MeasureRequest measureRequest) throws NotFoundException;
    // 계측기 단일 조회
    MeasureResponse getMeasure(Long id) throws NotFoundException;
    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
    Page<MeasureResponse> getMeasures(Long gaugeId, Long month, Pageable pageable);
    // 계측기 수정
    MeasureResponse updateMeasure(Long id, MeasureRequest measureRequest) throws NotFoundException;
    // 계측기 삭제
    void deleteMeasure(Long id) throws NotFoundException;
}
