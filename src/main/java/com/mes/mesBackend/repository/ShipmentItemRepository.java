package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.entity.ShipmentItem;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ShipmentItemRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentItemRepository extends JpaCustomRepository<ShipmentItem, Long>, ShipmentItemRepositoryCustom {
    Optional<ShipmentItem> findByShipmentAndDeleteYnFalse(Shipment shipment);
    Optional<ShipmentItem> findByIdAndShipmentAndDeleteYnFalse(Long id, Shipment shipment);
}
