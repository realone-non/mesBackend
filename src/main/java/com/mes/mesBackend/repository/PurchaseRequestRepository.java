package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PurchaseOrder;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.PurchaseRequestRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRequestRepository extends JpaCustomRepository<PurchaseRequest, Long>, PurchaseRequestRepositoryCustom {
    Optional<PurchaseRequest> findByIdAndPurchaseOrderAndDeleteYnFalse(Long id, PurchaseOrder purchaseOrder);
    // 해당 구매발주에 해당하는 구매요청정보들 조회
    List<PurchaseRequest> findAllByPurchaseOrderAndDeleteYnFalse(PurchaseOrder purchaseOrder);
}
