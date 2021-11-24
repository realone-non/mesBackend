package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.repository.custom.EstimateRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateRepository extends JpaCustomRepository<Estimate, Long> , EstimateRepositoryCustom {
}