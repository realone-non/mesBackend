package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.QBomMaster;
import com.mes.mesBackend.repository.custom.BomMasterRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class BomMasterRepositoryImpl implements BomMasterRepositoryCustom {
    // 검색조건: 품목계정, 품목그룹, 품번|품명

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QBomMaster bomMaster = QBomMaster.bomMaster;

    @Override
    public List<BomMaster> findAllByCondition(
            Long itemAccountId,
            Long itemGroupId,
            String itemNoOrName
    ) {
        return jpaQueryFactory
                .selectFrom(bomMaster)
                .where(
                        isItemAccountEq(itemAccountId),
                        isItemGroupEq(itemGroupId),
                        isItemNoOrItemNameToItemNoOrItemName(itemNoOrName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목 계정 검색
    private BooleanExpression isItemAccountEq(Long itemAccountId) {
        return itemAccountId != null ? bomMaster.item.itemAccount.id.eq(itemAccountId) : null;
    }

    // 품목 그룹 검색
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? bomMaster.item.itemGroup.id.eq(itemGroupId) :  null;
    }

    // 품명|품번 검색
    private BooleanExpression isItemNoOrItemNameToItemNoOrItemName(String itemNoOrName) {
        return itemNoOrName != null ? bomMaster.item.itemNo.contains(itemNoOrName)
                .or(bomMaster.item.itemName.contains(itemNoOrName)) : null;
    }

    // 삭제여부 false만 검색
    private BooleanExpression isDeleteYnFalse() {
        return bomMaster.deleteYn.isFalse();
    }

//    @Override
//    public Page<BomMaster> findAllByCondition(
//            Long itemAccountId,
//            Long itemGroupId,
//            String itemNoOrName,
//            Pageable pageable
//    ) {
//        QueryResults<BomMaster> results = jpaQueryFactory
//                .selectFrom(bomMaster)
//                .where(
//                        isItemAccountEq(itemAccountId),
//                        isItemGroupEq(itemGroupId),
//                        isItemNoOrItemNameToItemNoOrItemName(itemNoOrName),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
