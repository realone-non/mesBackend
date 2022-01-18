package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.MaterialStockInspectRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRequestRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialStockInspectRequestRepository extends JpaCustomRepository<MaterialStockInspectRequest, Long>, MaterialStockInspectRequestRepositoryCustom {
}
