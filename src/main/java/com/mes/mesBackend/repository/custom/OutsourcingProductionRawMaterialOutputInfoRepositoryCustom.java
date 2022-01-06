package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;

import java.util.List;
import java.util.Optional;

public interface OutsourcingProductionRawMaterialOutputInfoRepositoryCustom {
    //외주생산 원재료 출고 대상 정보 리스트 조회
    List<OutsourcingMaterialReleaseResponse> findAllUseYn(Long prodId);

    //외주생산 원재료 출고 대상 정보 단일 조회
    Optional<OutsourcingMaterialReleaseResponse> findByMaterialId(Long id);
}
