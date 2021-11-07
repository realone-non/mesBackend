package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseTypeRepository extends JpaCustomRepository<WareHouseType, Long> {
}