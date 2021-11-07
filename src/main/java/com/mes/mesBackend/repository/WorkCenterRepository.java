package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenter;
import com.mes.mesBackend.entity.WorkCenterCode;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCenterRepository extends JpaCustomRepository<WorkCenter, Long> {
    boolean existsAllByWorkCenterCodeAndDeleteYnFalse(WorkCenterCode workCenterCode);
}