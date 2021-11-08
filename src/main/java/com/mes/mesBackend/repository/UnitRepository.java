package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Unit;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaCustomRepository<Unit, Long> {
}