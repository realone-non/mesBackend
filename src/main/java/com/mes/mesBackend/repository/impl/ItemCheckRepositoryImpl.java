package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.ItemCheck;
import com.mes.mesBackend.entity.QItemCheck;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.repository.custom.ItemCheckRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class ItemCheckRepositoryImpl implements ItemCheckRepositoryCustom {
    // 품목별 검사항목 전체 조회 /  검색조건: 검사유형, 품목그룹, 품목계정

    private final JPAQueryFactory jpaQueryFactory;

    final QItemCheck itemCheck = QItemCheck.itemCheck;

    @Override @Transactional(readOnly = true)
    public List<ItemCheck> findAllCondition(
            TestCategory testCategory,
            Long itemGroup,
            Long itemAccount
    ) {
        return jpaQueryFactory
                .selectFrom(itemCheck)
                .where(
                        isTestCategoryEq(testCategory),
                        isItemGroupEq(itemGroup),
                        isItemAccountEq(itemAccount),
                        isDeleteYnFalse()
                )
                .fetch();
    }


    // 검사유형으로 조회
    private BooleanExpression isTestCategoryEq(TestCategory testCategory) {
        return testCategory != null ? itemCheck.checkCategory.eq(testCategory) :  null;
    }


    // 픔목그룹 조회
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? itemCheck.item.itemGroup.id.eq(itemGroupId) : null;
    }

    // 품목계정 조회
    private BooleanExpression isItemAccountEq(Long itemAccountId) {
        return itemAccountId != null ? itemCheck.item.itemAccount.id.eq(itemAccountId) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return itemCheck.deleteYn.isFalse();
    }

//    @Override
//    public Page<ItemCheck> findAllCondition(
//            TestCategory testCategory,
//            Long itemGroup,
//            Long itemAccount,
//            Pageable pageable
//    ) {
//        QueryResults<ItemCheck> results = jpaQueryFactory
//                .selectFrom(itemCheck)
//                .where(
//                        isTestCategoryEq(testCategory),
//                        isItemGroupEq(itemGroup),
//                        isItemAccountEq(itemAccount),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
