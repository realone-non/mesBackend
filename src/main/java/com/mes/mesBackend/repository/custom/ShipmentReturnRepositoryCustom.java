package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ShipmentReturnResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 4-6. 출하반품 등록
public interface ShipmentReturnRepositoryCustom {
    // 출하반품 리스트 검색 조회, 검색조건: 거래처 id, 품번|품명, 반품기간 fromDate~toDate
    List<ShipmentReturnResponse> findShipmentReturnResponsesByCondition(Long clientId, String itemNoAndItemName, LocalDate fromDate, LocalDate toDate);
    // 출하반품 단일 조회
    Optional<ShipmentReturnResponse> findShipmentReturnResponseByIdAndDeleteYnFalse(Long id);
}
