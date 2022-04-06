package com.mes.mesBackend.repository;

import com.mes.mesBackend.dto.response.RecycleLotResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.OutSourcingInput;
import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.LotMasterRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotMasterRepository extends JpaCustomRepository<LotMaster, Long>, LotMasterRepositoryCustom {
    Optional<LotMaster> findByPurchaseInputAndDeleteYnFalse(PurchaseInput purchaseInput);
    Optional<LotMaster> findByOutSourcingInputAndDeleteYnFalse(OutSourcingInput outSourcingInput);

    LotMaster findByLotNoAndUseYnTrue(String lotNo);
    Optional<LotMaster> findByLotNoAndDeleteYnFalse(String lotNo);

}
