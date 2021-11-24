package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.QItem;
import com.mes.mesBackend.repository.custom.ItemRepositoryCustom;
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
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QItem item = QItem.item;

    // 품목그룹, 품목계정, 품번, 품명, 검색어
    @Override
    public List<Item> findAllByCondition(
            Long itemGroupId,
            Long itemAccountId,
            String itemNo,
            String itemName,
            String searchWord
    ) {
        return jpaQueryFactory
                .selectFrom(item)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemAccountEq(itemAccountId),
                        isItemNoContaining(itemNo),
                        isItemNameContaining(itemName),
                        isSearchContaining(searchWord),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 품목그룹 검색
    private BooleanExpression isItemGroupEq(Long itemGroup) {
        return itemGroup != null ? item.itemGroup.id.eq(itemGroup) : null;
    }

    // 품목계정 검색
    private BooleanExpression isItemAccountEq(Long itemAccount) {
        return itemAccount != null ? item.itemAccount.id.eq(itemAccount) : null;
    }

    // 품번 검색
    private BooleanExpression isItemNoContaining(String itemNo) {
        return itemNo !=  null ? item.itemNo.contains(itemNo) : null;
    }

    // 품명 검색
    private BooleanExpression isItemNameContaining(String itemName) {
        return itemName != null ? item.itemName.contains(itemName) : null;
    }

    // 검색어 검색
    private BooleanExpression isSearchContaining(String searchWord) {
        return searchWord!= null ? item.searchWord.contains(searchWord) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return item.deleteYn.isFalse();
    }

//    @Override
//    public Page<Item> findAllByCondition(
//            Long itemGroupId,
//            Long itemAccountId,
//            String itemNo,
//            String itemName,
//            String searchWord,
//            Pageable pageable
//    ) {
//        QueryResults<Item> results = jpaQueryFactory
//                .selectFrom(item)
//                .where(
//                        isItemGroupEq(itemGroupId),
//                        isItemAccountEq(itemAccountId),
//                        isItemNoContaining(itemNo),
//                        isItemNameContaining(itemName),
//                        isSearchContaining(searchWord),
//                        isDeleteYnFalse()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//    }
}
