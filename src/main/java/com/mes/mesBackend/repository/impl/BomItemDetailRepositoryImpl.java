package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.BomItemDetail;
import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.QBomItemDetail;
import com.mes.mesBackend.repository.custom.BomItemDetailRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class BomItemDetailRepositoryImpl implements BomItemDetailRepositoryCustom {
    // 검색조건: 품목|품명

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;

    @Override
    public List<BomItemDetail> findAllByCondition(
            BomMaster bomMaster,
            String itemNoOrItemName
    ) {
        return jpaQueryFactory
                .selectFrom(bomItemDetail)
                .where(
                        isBomMasterEq(bomMaster),
                        isItemNoOrItemNameToItemNoOrItemName(itemNoOrItemName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목|품명으로 검색
    private BooleanExpression isItemNoOrItemNameToItemNoOrItemName(String itemNoOrItemName) {
        return itemNoOrItemName != null ? bomItemDetail.item.itemNo.contains(itemNoOrItemName)
                .or(bomItemDetail.item.itemName.contains(itemNoOrItemName)) : null;
    }

    // BOM master Id 로 검색
    private BooleanExpression isBomMasterEq(BomMaster bomMaster) {
        return bomMaster != null ? bomItemDetail.bomMaster.id.eq(bomMaster.getId()) : null;
    }

    // 삭제여부 false
    private BooleanExpression isDeleteYnFalse() {
        return bomItemDetail.deleteYn.isFalse();
    }
}
