package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ProductionPerformanceRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionPerformanceRepository extends JpaCustomRepository<ProductionPerformance, Long>, ProductionPerformanceRepositoryCustom {
    Optional<ProductionPerformance> findByWorkOrderDetailAndLotMasterAndDeleteYnFalse(WorkOrderDetail workOrderDetail, LotMaster lotMaster);
}
