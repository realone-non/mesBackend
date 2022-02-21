package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLineRepository extends JpaCustomRepository<WorkLine, Long> {
}