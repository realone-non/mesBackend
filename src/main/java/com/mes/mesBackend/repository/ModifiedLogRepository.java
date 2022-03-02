package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.repository.custom.ModifiedLogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModifiedLogRepository extends JpaRepository<ModifiedLog, Long>, ModifiedLogRepositoryCustom {
}
