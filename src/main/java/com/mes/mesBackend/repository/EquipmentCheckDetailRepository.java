package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.EquipmentCheckDetail;
import com.mes.mesBackend.repository.custom.EquipmentCheckDetailRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 17-1. 설비점검 실적 등록
@Repository
public interface EquipmentCheckDetailRepository extends JpaCustomRepository<EquipmentCheckDetail, Long>, EquipmentCheckDetailRepositoryCustom {
    Optional<EquipmentCheckDetail> findByIdAndEquipmentAndDeleteYnFalse(Long id, Equipment equipment);
}
