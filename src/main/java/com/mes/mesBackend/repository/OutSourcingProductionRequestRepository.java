package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.OutSourcingProductionRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.OutsourcingRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutSourcingProductionRequestRepository extends JpaCustomRepository<OutSourcingProductionRequest, Long>, OutsourcingRepositoryCustom {
}
