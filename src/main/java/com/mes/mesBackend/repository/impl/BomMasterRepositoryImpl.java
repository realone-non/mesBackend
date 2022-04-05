package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.QBomItemDetail;
import com.mes.mesBackend.entity.QBomMaster;
import com.mes.mesBackend.entity.QItem;
import com.mes.mesBackend.repository.custom.BomMasterRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BomMasterRepositoryImpl implements BomMasterRepositoryCustom {
    // 검색조건: 품목계정, 품목그룹, 품번|품명

    private final JPAQueryFactory jpaQueryFactory;

    final QBomMaster bomMaster = QBomMaster.bomMaster;
    final QItem item = QItem.item;
    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;

    @Transactional(readOnly = true)
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
                .orderBy(bomMaster.createdDate.desc())
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

    // 입력받은 item 이 bomMaster 에 이미 있는지 여부
    @Override
    public boolean existsByItemInBomMasters(Long itemId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(bomMaster)
                .leftJoin(item).on(item.id.eq(bomMaster.item.id))
                .where(
                        bomMaster.item.id.eq(itemId),
                        isDeleteYnFalse()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    // bomMaster 의 itemId 와 bomItemDetail 의 itemId 로 bomItemDetail 조회
    @Override
    public Optional<Float> findBomItemDetailByBomMasterItemAndDetailItem(Long bomMasterItemId, Long bomItemid) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(bomItemDetail.amount)
                        .from(bomItemDetail)
                        .where(
                                bomMaster.item.id.eq(bomMasterItemId),
                                bomMaster.deleteYn.isFalse(),
                                bomItemDetail.item.id.eq(bomItemid),
                                bomItemDetail.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    // bomDetail 에 해당하는 item 인지
    @Override
    public boolean existsBomItemDetailByItemId(Long bomMasterItemId, Long bomDetailItemId) {
        Integer fetchOne =
                jpaQueryFactory
                        .selectOne()
                        .from(bomItemDetail)
                        .where(
                                bomItemDetail.bomMaster.item.id.eq(bomMasterItemId),
                                bomItemDetail.bomMaster.deleteYn.isFalse(),
                                bomItemDetail.item.id.eq(bomDetailItemId),
                                bomItemDetail.deleteYn.isFalse()
                        )
                        .fetchFirst();

        return fetchOne != null;
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
