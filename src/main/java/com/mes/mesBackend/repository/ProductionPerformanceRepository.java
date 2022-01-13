package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ProductionPerformanceRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionPerformanceRepository extends JpaCustomRepository<ProductionPerformance, Long>, ProductionPerformanceRepositoryCustom {
}
