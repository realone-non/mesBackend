package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.entity.ShipmentItem;
import com.mes.mesBackend.entity.enumeration.OrderState;

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
    // 출하정보 리스트 조회
    List<PopShipmentResponse> findPopShipmentResponseByCondition(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn);
    // 출하 lot 가 등록되어 있는 출하품목만 가져옴
    List<ShipmentItem> findShipmentItemByShipmentId(Long shipmentId);
    // 출하에 해당되는 출하 lot 정보의 모든 shipmentAmount
    List<Integer> findShipmentLotShipmentAmountByShipmentId(Long shipmentId);
    // 오늘 날짜로 생성 된 shipment 중 barcodeNumber 만 가져옴(내림차순 limit 1)
    Optional<String> findBarcodeNumberByToday(LocalDate now);
    // 출하일자가 오늘인거 갯수
    Optional<Long> findShipmentCountByToday(OrderState orderState);
    // 매출관련현황 - 제품 출고
    // 현재 달에 가장 많이 출고 된 품목 5개
    List<ItemResponse.noAndName> findSalesRelatedStatusResponseByShipmentItems(LocalDate fromDate, LocalDate toDate);
    // 주 별로 출하 된 품목 갯수
    Optional<Integer> findShipmentAmountByWeekDate(LocalDate fromDate, LocalDate toDate, Long itemId);
}
