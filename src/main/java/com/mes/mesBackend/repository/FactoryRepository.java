package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Factory;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaCustomRepository<Factory, Long> {
}