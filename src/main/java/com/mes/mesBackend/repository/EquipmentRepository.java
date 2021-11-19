package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaCustomRepository<Equipment, Long> {

}