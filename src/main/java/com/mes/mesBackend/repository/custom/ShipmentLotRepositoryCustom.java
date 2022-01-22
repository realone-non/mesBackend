package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ShipmentLotInfoResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.util.List;
import java.util.Optional;

public interface ShipmentLotRepositoryCustom {
    // lotMaster: shipmentItem 의 item 에 해당되는 lotMaster 가져옴, 조건? 공정이 포장까지 완료된, stockAmount 가 1 이상
    List<Long> findLotMasterIdByItemIdAndWorkProcessShipment(Long itemId, WorkProcessDivision workProcessDivision);
    // 단일조회
    Optional<ShipmentLotInfoResponse> findShipmentLotResponseById(Long id);
    // 전체조회
    List<ShipmentLotInfoResponse> findShipmentLotResponsesByShipmentItemId(Long shipmentItemId);
    // 출하 품목정보에 해당하는 LOT 정보가 있는지 여부
    boolean existsByShipmentItemInShipmentLot(Long shipmentId);
}
