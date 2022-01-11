package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentBreakdown;
import com.mes.mesBackend.repository.custom.EquipmentBreakdownRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

// 17-2. 설비 고장 수리내역 등록
@Repository
public interface EquipmentBreakdownRepository extends JpaCustomRepository<EquipmentBreakdown, Long>, EquipmentBreakdownRepositoryCustom {
}
