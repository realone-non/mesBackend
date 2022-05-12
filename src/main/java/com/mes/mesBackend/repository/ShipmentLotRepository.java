package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ShipmentItem;
import com.mes.mesBackend.entity.ShipmentLot;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ShipmentLotRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentLotRepository extends JpaCustomRepository<ShipmentLot, Long>, ShipmentLotRepositoryCustom {
    Optional<ShipmentLot> findByIdAndShipmentItemAndDeleteYnFalse(Long id, ShipmentItem shipmentItem);
    List<ShipmentLot> findByShipmentItemAndDeleteYnFalse(ShipmentItem shipmentItem);
}
