package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.repository.custom.LotLogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotLogRepository extends JpaRepository<LotLog, Long>, LotLogRepositoryCustom {
    Optional<LotLog> findByWorkOrderDetailAndLotMaster(WorkOrderDetail workOrderDetail, LotMaster lotMaster);
}
