package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.ContractItem;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ProduceOrderRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduceOrderRepository extends JpaCustomRepository<ProduceOrder, Long>, ProduceOrderRepositoryCustom {
    boolean existsByContractAndDeleteYnFalse(Contract contract);
    boolean existsByContractItemAndDeleteYnFalse(ContractItem contractItem);
}
