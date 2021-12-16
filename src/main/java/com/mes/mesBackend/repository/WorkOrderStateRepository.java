package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.WorkOrderState;
import com.mes.mesBackend.repository.custom.WorkOrderStateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderStateRepository extends JpaRepository<WorkOrderState, Long>, WorkOrderStateRepositoryCustom {
    List<WorkOrderState> findAllByWorkOrderDetail(WorkOrderDetail workOrderDetail);
}