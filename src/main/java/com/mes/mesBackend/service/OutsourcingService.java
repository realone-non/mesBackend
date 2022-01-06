package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.OutsourcingMaterialReleaseRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutsourcingService {
    //외주생산의뢰 등록
    OutsourcingProductionResponse createOutsourcingProduction(OutsourcingProductionRequestRequest outsourcingProductionRequestRequest);

    //외주생산의뢰 리스트조회
    List<OutsourcingProductionResponse> getOutsourcingProductions(Long clientId, Long itemNo, LocalDate startDate, LocalDate endDate);

    //외주생산의뢰 조회
    Optional<OutsourcingProductionResponse> getOutsourcingProduction(Long id);

    //외주생산의뢰 수정
    OutsourcingProductionResponse modifyOutsourcingProduction(Long id, OutsourcingProductionRequestRequest outsourcingProduction);

    //외주생산의뢰 삭제
    void deleteOutsourcingProduction(Long id);

    //외주생산 원재료 출고 대상 등록
    Optional<OutsourcingMaterialReleaseResponse> createOutsourcingMaterial(OutsourcingMaterialReleaseRequest outsourcingMaterialReleaseRequest);

    //외주생산 원재료 출고 대상 리스트 조회
    List<OutsourcingMaterialReleaseResponse> getOutsourcingMeterials(Long productionId);

    //외주생산 원재료 출고 대상 단일 조회
    Optional<OutsourcingMaterialReleaseResponse> getOutsourcingMaterial(Long materialId);

    //외주생산 원재료 출고 대상 수정
    Optional<OutsourcingMaterialReleaseResponse> modifyOutsourcingMaterial(Long materialId, OutsourcingMaterialReleaseRequest request);

    //외주생산 원재료 출고 대상 삭제
    void deleteOutsourcingMaterial(Long id);
}
