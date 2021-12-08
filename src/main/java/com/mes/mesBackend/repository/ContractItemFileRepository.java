package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ContractItem;
import com.mes.mesBackend.entity.ContractItemFile;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractItemFileRepository extends JpaCustomRepository<ContractItemFile, Long> {
    List<ContractItemFile> findAllByContractItemAndDeleteYnFalse(ContractItem contractItem);
    ContractItemFile findByIdAndContractItemAndDeleteYnFalse(Long id, ContractItem contractItem);
    int countAllByContractItemAndDeleteYnFalse(ContractItem contractItem);
}