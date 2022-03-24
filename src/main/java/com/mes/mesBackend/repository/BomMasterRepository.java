package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.repository.custom.BomMasterRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomMasterRepository extends JpaCustomRepository<BomMaster, Long> , BomMasterRepositoryCustom {
    // item 이 bomMaster 에 존재하는지
    boolean existsByItemAndDeleteYnIsFalse(Item item);
}