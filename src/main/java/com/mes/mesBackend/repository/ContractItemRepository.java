package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.ContractItem;
import com.mes.mesBackend.repository.custom.ContractItemStateRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractItemRepository extends JpaCustomRepository<ContractItem, Long> {
    Optional<ContractItem> findByIdAndContractAndDeleteYnFalse(Long id, Contract contract);
    List<ContractItem> findAllByContractAndDeleteYnFalse(Contract contract);
}