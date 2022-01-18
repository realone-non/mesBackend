package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.MaterialStockInspect;
import com.mes.mesBackend.entity.MaterialStockInspectRequest;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.MaterialStockInspectRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialStockInspectRepository extends JpaCustomRepository<MaterialStockInspect, Long>, MaterialStockInspectRepositoryCustom {
    //재고조사의뢰 ID로 가져오기
    List<MaterialStockInspect> findAllByDeleteYnFalseAndMaterialStockInspectRequest(MaterialStockInspectRequest request);
}
