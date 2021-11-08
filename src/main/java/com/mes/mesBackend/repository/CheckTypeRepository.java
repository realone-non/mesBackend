package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CheckType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckTypeRepository extends JpaCustomRepository<CheckType, Long> {
}