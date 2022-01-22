package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ShipmentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 4-5. 출하등록
public interface ShipmentRepositoryCustom {
    // 출하 등록 리스트 검색 조회: 거래처 명, 출하기간, 화폐 id, 담당자 명
    List<ShipmentResponse> findShipmentResponsesByCondition(Long clientId, LocalDate fromDate, LocalDate toDate, Long currencyId, Long userId);
    // 출하 등록 단일 조회
    Optional<ShipmentResponse> findShipmentResponseById(Long id);
    // 출하에 등록된 품목정보
    boolean existsByShipmentItemInShipment(Long shipmentId);
}
