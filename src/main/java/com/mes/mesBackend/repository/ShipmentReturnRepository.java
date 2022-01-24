package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ShipmentReturn;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ShipmentReturnRepositoryCustom;
import org.springframework.stereotype.Repository;

// 4-6. 출하반품 등록
@Repository
public interface ShipmentReturnRepository extends JpaCustomRepository<ShipmentReturn, Long>, ShipmentReturnRepositoryCustom {
}
