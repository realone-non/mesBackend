package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PurchaseOrder;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.PurchaseOrderRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaCustomRepository<PurchaseOrder, Long>, PurchaseOrderRepositoryCustom {
}
