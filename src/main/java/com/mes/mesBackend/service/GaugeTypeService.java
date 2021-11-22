package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.GaugeTypeRequest;
import com.mes.mesBackend.dto.response.GaugeTypeResponse;
import com.mes.mesBackend.entity.GaugeType;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface GaugeTypeService {
    // Gauge 유형 생성
    GaugeTypeResponse createGaugeType(GaugeTypeRequest gaugeTypeRequest);
    // Gauge 유형 단일 조회
    GaugeTypeResponse getGaugeType(Long id) throws NotFoundException;
    // Gauge 유형 전체 조회
    List<GaugeTypeResponse> getGaugeTypes();
    // Gauge 유형 수정
    GaugeTypeResponse updateGaugeType(Long id, GaugeTypeRequest gaugeTypeRequest) throws NotFoundException;
    // Gauge 유형 삭제
    void deleteGaugeType(Long id) throws NotFoundException;
    // Gauge 유형 단일 조회 및 예외
    GaugeType getGaugeTypeOrThrow(Long id) throws NotFoundException;
}
