package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.BomItemDetail;
import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.util.List;
import java.util.Optional;

public interface BomMasterRepositoryCustom {
//    검색조건: 품목계정, 품목그룹, 품번|품명
    List<BomMaster> findAllByCondition(Long itemAccountId, Long itemGroupId, String itemNoOrName);
//    Page<BomMaster> findAllByCondition(Long itemAccountId, Long itemGroupId, String itemNoOrName, Pageable pageable);
    // 입력받은 item 이 bomMaster 에 이미 있는지 여부
    boolean existsByItemInBomMasters(Long itemId);
    // bomMaster 의 itemId 와 bomItemDetail 의 itemId 로 bomItemDetail 조회
    Optional<Float> findBomItemDetailByBomMasterItemAndDetailItem(Long bomMasterItemId, Long bomItemid);
    // bomDetail 에 해당하는 item 인지
    boolean existsBomItemDetailByItemId(Long bomMasterItemId, Long bomDetailItemId);
    // item 으로 bom 조회
    Optional<Long> findByItemIdAndDeleteYnFalse(Long bomMasterItemId);
    List<BomItemDetail> findByItemIdAndWorkProcessDivision(Long itemId, WorkProcessDivision workProcessDivision);

}
