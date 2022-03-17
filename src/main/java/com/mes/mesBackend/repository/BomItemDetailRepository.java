package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BomItemDetail;
import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.repository.custom.BomItemDetailRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BomItemDetailRepository extends JpaCustomRepository<BomItemDetail, Long> , BomItemDetailRepositoryCustom {
    Optional<BomItemDetail> findByBomMasterAndIdAndDeleteYnFalse(BomMaster bomMaster, Long id);
    // item 이 bomItemDetail 에 존재하는지
    boolean existsByItemAndDeleteYnIsFalse(Item item);
    // bomMaster 가 bomItemDetail 에 존재하는지
    boolean existsByBomMasterAndDeleteYnFalse(BomMaster bomMaster);
}