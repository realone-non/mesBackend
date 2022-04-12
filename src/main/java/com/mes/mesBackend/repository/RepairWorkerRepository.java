package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentBreakdown;
import com.mes.mesBackend.entity.RepairWorker;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 17-2. 수리작업자 정보
@Repository
public interface RepairWorkerRepository extends JpaCustomRepository<RepairWorker, Long> {
    List<RepairWorker> findAllByEquipmentBreakdownAndDeleteYnFalseOrderByCreatedDate(EquipmentBreakdown equipmentBreakdown);
    Optional<RepairWorker> findByIdAndEquipmentBreakdownAndDeleteYnFalse(Long id, EquipmentBreakdown equipmentBreakdown);
}
