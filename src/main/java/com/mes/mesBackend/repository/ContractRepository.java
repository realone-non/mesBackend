package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.repository.custom.ContractRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaCustomRepository<Contract, Long> , ContractRepositoryCustom {
}