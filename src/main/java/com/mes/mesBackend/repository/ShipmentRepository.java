package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ShipmentRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 4-5. 출하등록
@Repository
public interface ShipmentRepository extends JpaCustomRepository<Shipment, Long>, ShipmentRepositoryCustom {
    Optional<Shipment> findByBarcodeNumberAndDeleteYnFalse(String barcodeNumber);
}
