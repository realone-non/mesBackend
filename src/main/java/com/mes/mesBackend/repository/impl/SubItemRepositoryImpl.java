package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QSubItem;
import com.mes.mesBackend.entity.SubItem;
import com.mes.mesBackend.repository.custom.SubItemRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class SubItemRepositoryImpl implements SubItemRepositoryCustom {
    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QSubItem subItem = QSubItem.subItem1;

    @Override
    public List<SubItem> findAllCondition(
            Long itemGroupId,
            Long itemAccountId,
            String itemNo,
            String itemName
    ) {
        return jpaQueryFactory
                .selectFrom(subItem)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemAccountEq(itemAccountId),
                        isItemNoContains(itemNo),
                        isItemNameContains(itemName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목그룹으로 조회
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? subItem.item.itemGroup.id.eq(itemGroupId) : null;
    }

    // 품목계정으로 조회
    private BooleanExpression isItemAccountEq(Long itemAccountId) {
        return itemAccountId != null ? subItem.item.itemAccount.id.eq(itemAccountId) : null;
    }

    // 품번으로 조회
    private BooleanExpression isItemNoContains(String itemNo) {
        return itemNo != null ? subItem.item.itemNo.contains(itemNo) : null;
    }

    // 품명으로 조회
    private BooleanExpression isItemNameContains(String itemName) {
        return itemName != null ? subItem.item.itemName.contains(itemName) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return subItem.deleteYn.isFalse();
    }

    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명

//    @Autowired
//    JPAQueryFactory jpaQueryFactory;
//
//    final QSubItem subItem = QSubItem.subItem1;
//
//    @Override
//    public Page<SubItem> findAllCondition(
//            Long itemGroupId,
//            Long itemAccountId,
//            String itemNo,
//            String itemName,
//            Pageable pageable
//    ) {
//        QueryResults<SubItem> results = jpaQueryFactory
//                .selectFrom(subItem)
//                .where(
//                        isItemGroupEq(itemGroupId),
//                        isItemAccountEq(itemAccountId),
//                        isItemNoContains(itemNo),
//                        isItemNameContains(itemName)
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
