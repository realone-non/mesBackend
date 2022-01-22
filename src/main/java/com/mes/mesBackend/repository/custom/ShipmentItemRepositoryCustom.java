package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ShipmentItemRepositoryCustom {
    // shipmentItem 에 해당되는 제일 처음 등록된 contract 조회
    Optional<Contract> findContractsByShipmentId(Long shipmentId);
    // 출하 품목정보 단일 조회
    Optional<ShipmentItemResponse> findShipmentItemResponseByShipmentItemId(Long shipmentId, Long shipmentItemId);
    // 출하 품목정보 전체 조회
    List<ShipmentItemResponse> findShipmentResponsesByShipmentId(Long shipmentId);
    // 출하에 수주품목이 있는지
    boolean existsByContractItemInShipment(Long shipmentId, Long contractItemId);
}
