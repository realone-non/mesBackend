package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotLogRepository extends JpaRepository<LotLog, Long> {
}
