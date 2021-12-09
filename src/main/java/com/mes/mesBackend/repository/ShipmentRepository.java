package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ShipmentRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaCustomRepository<Shipment, Long> , ShipmentRepositoryCustom {
}
