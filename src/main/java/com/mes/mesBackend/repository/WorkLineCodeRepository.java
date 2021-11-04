package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkLineCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLineCodeRepository extends JpaRepository<WorkLineCode, Long> {
}