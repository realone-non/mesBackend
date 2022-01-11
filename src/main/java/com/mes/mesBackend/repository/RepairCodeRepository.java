package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.RepairCode;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

// 수리코드
@Repository
public interface RepairCodeRepository extends JpaCustomRepository<RepairCode, Long> {
}
