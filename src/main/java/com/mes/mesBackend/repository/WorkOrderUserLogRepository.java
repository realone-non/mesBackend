package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkOrderUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 작업지시 작업자 정보 변경 기록
@Repository
public interface WorkOrderUserLogRepository extends JpaRepository<WorkOrderUserLog, Long> {
}
