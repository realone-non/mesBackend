package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.WorkOrderDetailRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 6-3. 생산계획 수립
// 6-2. 작업지시 등록
// 8-1. 작지상태 확인
@Repository
public interface WorkOrderDetailRepository extends JpaCustomRepository<WorkOrderDetail, Long>, WorkOrderDetailRepositoryCustom {
    Optional<WorkOrderDetail> findByIdAndProduceOrderAndDeleteYnFalse(Long id, ProduceOrder produceOrder);
    boolean existsByProduceOrderAndDeleteYnFalse(ProduceOrder produceOrder);
}
