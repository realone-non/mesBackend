package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QWorkDocument;
import com.mes.mesBackend.entity.WorkDocument;
import com.mes.mesBackend.repository.custom.WorkDocumentRepositoryCustom;
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
public class WorkDocumentRepositoryImpl implements WorkDocumentRepositoryCustom {
    // 작업표준서 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QWorkDocument workDocument = QWorkDocument.workDocument;

    @Override
    public List<WorkDocument> findAllByCondition(
            Long itemGroupId,
            Long itemAccountId,
            String itemNo,
            String itemName
    ) {
        return jpaQueryFactory
                .selectFrom(workDocument)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemAccountEq(itemAccountId),
                        isItemNoContaining(itemNo),
                        isItemNameContaining(itemName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목 그룹 조회
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? workDocument.item.itemGroup.id.eq(itemGroupId) : null;
    }

    // 품목 계정 조회
    private BooleanExpression isItemAccountEq(Long itemAccountId) {
        return itemAccountId != null ? workDocument.item.itemAccount.id.eq(itemAccountId) : null;
    }

    // 품번으로 조회
    private BooleanExpression isItemNoContaining(String itemNo) {
        return itemNo != null ? workDocument.item.itemNo.contains(itemNo) : null;
    }

    // 품명으로 조회
    private BooleanExpression isItemNameContaining(String itemName) {
        return itemName != null ? workDocument.item.itemName.contains(itemName) :  null;
    }

    // 삭제여부
    private BooleanExpression isDeleteYnFalse() {
        return workDocument.deleteYn.isFalse();
    }

//    @Override
//    public Page<WorkDocument> findAllByCondition(
//            Long itemGroupId,
//            Long itemAccountId,
//            String itemNo,
//            String itemName,
//            Pageable pageable
//    ) {
//        QueryResults<WorkDocument> results = jpaQueryFactory
//                .selectFrom(workDocument)
//                .where(
//                        isItemGroupEq(itemGroupId),
//                        isItemAccountEq(itemAccountId),
//                        isItemNoContaining(itemNo),
//                        isItemNameContaining(itemName),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
