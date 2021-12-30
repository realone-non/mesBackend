package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.PurchaseInputRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseInputRepository extends JpaCustomRepository<PurchaseInput, Long>, PurchaseInputRepositoryCustom {
    Optional<PurchaseInput> findByIdAndPurchaseRequestAndDeleteYnFalse(Long purchaseInputId, PurchaseRequest purchaseRequest);
}
