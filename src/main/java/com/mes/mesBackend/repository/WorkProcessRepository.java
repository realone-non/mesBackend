package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.entity.WorkProcessCode;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkProcessRepository extends JpaCustomRepository<WorkProcess, Long> {
    boolean existsByWorkProcessCodeAndDeleteYnFalse(WorkProcessCode workProcessCode);
}