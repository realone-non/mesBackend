package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.OutSourcingProductionRawMaterialOutputInfo;
import com.mes.mesBackend.entity.OutSourcingProductionRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.OutsourcingProductionRawMaterialOutputInfoRepositoryCustom;
import com.mes.mesBackend.repository.custom.OutsourcingRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutSourcingProductionRawMaterialOutputInfoRepository extends JpaCustomRepository<OutSourcingProductionRawMaterialOutputInfo, Long>, OutsourcingProductionRawMaterialOutputInfoRepositoryCustom {
}
