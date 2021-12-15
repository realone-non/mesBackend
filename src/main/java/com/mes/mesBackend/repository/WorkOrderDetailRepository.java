package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.WorkOrderDetailRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderDetailRepository extends JpaCustomRepository<WorkOrderDetail, Long>, WorkOrderDetailRepositoryCustom {
    Optional<WorkOrderDetail> findByIdAndProduceOrderAndDeleteYnFalse(Long id, ProduceOrder produceOrder);
    List<WorkOrderDetail> findAllByProduceOrderAndDeleteYnFalse(ProduceOrder produceOrder);
}
