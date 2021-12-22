package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.PurchaseRequestRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaCustomRepository<PurchaseRequest, Long>, PurchaseRequestRepositoryCustom {
}
