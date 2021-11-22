package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.GaugeType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GaugeTypeRepository extends JpaCustomRepository<GaugeType, Long> {
}