package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.repository.custom.EquipmentRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

// 3-5-1. 설비등록
@Repository
public interface EquipmentRepository extends JpaCustomRepository<Equipment, Long>, EquipmentRepositoryCustom {

}