package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentBreakdown;
import com.mes.mesBackend.entity.RepairItem;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairItemRepository extends JpaCustomRepository<RepairItem, Long> {
    Optional<RepairItem> findByIdAndEquipmentBreakdownAndDeleteYnFalse(Long id, EquipmentBreakdown equipmentBreakdown);
    List<RepairItem> findAllByEquipmentBreakdownAndDeleteYnFalse(EquipmentBreakdown equipmentBreakdown);
}
