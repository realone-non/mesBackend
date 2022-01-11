package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentBreakdown;
import com.mes.mesBackend.entity.EquipmentBreakdownFile;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentBreakdownFileRepository extends JpaCustomRepository<EquipmentBreakdownFile, Long> {
    Optional<EquipmentBreakdownFile> findByIdAndEquipmentBreakdownAndDeleteYnFalse(Long id, EquipmentBreakdown equipmentBreakdown);
}
