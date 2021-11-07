package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.WorkCenterCheckRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCenterCheckRepository extends JpaCustomRepository<WorkCenterCheck, Long>, WorkCenterCheckRepositoryCustom {
}