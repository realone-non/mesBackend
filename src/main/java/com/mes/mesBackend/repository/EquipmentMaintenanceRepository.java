package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.EquipmentMaintenance;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentMaintenanceRepository extends JpaCustomRepository<EquipmentMaintenance, Long> {
}