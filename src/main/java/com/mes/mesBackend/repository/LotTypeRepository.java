package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotTypeRepository extends JpaCustomRepository<LotType, Long> {
}