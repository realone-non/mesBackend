package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentCheckDetail;
import com.mes.mesBackend.repository.custom.EquipmentCheckDetailRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentCheckDetailRepository extends JpaCustomRepository<EquipmentCheckDetail, Long>, EquipmentCheckDetailRepositoryCustom {
}
