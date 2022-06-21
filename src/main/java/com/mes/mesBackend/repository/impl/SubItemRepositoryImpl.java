package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QSubItem;
import com.mes.mesBackend.entity.SubItem;
import com.mes.mesBackend.repository.custom.SubItemRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class SubItemRepositoryImpl implements SubItemRepositoryCustom {
    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명


    private final JPAQueryFactory jpaQueryFactory;

    final QSubItem subItem = QSubItem.subItem1;

    @Override
    @Transactional(readOnly = true)
    public List<SubItem> findAllCondition(
            Long itemGroupId,
            Long itemAccountId,
            String itemNoAndItemName
    ) {
        return jpaQueryFactory
                .selectFrom(subItem)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemAccountEq(itemAccountId),
                        isDeleteYnFalse(),
                        isItemNoAndItemNameContain(itemNoAndItemName)
                )
                .orderBy(subItem.createdDate.desc())
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

    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? subItem.item.itemNo.contains(itemNoAndName).or(subItem.item.itemName.contains(itemNoAndName)) : null;
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
