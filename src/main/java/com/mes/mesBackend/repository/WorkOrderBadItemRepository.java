package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkOrderBadItem;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.WorkOrderBadItemRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderBadItemRepository extends JpaCustomRepository<WorkOrderBadItem, Long>, WorkOrderBadItemRepositoryCustom {

}
