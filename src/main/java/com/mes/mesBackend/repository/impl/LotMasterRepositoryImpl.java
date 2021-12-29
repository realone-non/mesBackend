package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.LotMasterRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class LotMasterRepositoryImpl implements LotMasterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QItemAccountCode itemAccountCode = QItemAccountCode.itemAccountCode;
    final QItemAccount itemAccount = QItemAccount.itemAccount;
    final QItem item = QItem.item;
    final QLotMaster lotMaster = QLotMaster.lotMaster;

    // id 로 itemAccountCode 의 symbol 조회
    @Override
    public ItemAccountCode findCodeByItemId(Long itemId) {
        return jpaQueryFactory
                .select(itemAccountCode)
                .from(itemAccountCode)
                .innerJoin(itemAccount).on(itemAccount.id.eq(itemAccountCode.itemAccount.id))
                .innerJoin(item).on(item.itemAccountCode.id.eq(itemAccountCode.id))
                .where(
                        item.id.eq(itemId),
                        item.deleteYn.isFalse()
                )
                .fetchOne();
    }

    // 원부자재 일련번호 시퀀스 찾는법
    // 일련번호의 1~6이 현재날짜의 format 과 동일 And 일련번호가 9자
    // 끝에서 두번째 자리 수 중 제일 큰 애를 찾아서  +1
    @Override
    public Optional<String> findLotNoByLotNoLengthAndLotNoDateAndCode(int length, String date, String code) {
        return Optional.ofNullable(jpaQueryFactory
                .select(lotMaster.lotNo)
                .from(lotMaster)
                .where(
                        isLotNoLengthEq(length),
                        isLotNoDateContain(date),
                        isCodeContain(code),
                        isDeleteYnFalse()
                )
                .orderBy(lotMaster.lotNo.desc())
                .limit(1)
                .fetchOne());
    }

    private BooleanExpression isLotNoLengthEq(int length) {
        return lotMaster.lotNo.length().eq(length);
    }

    private BooleanExpression isLotNoDateContain(String date) {
        return lotMaster.lotNo.contains(date);
    }

    private BooleanExpression isDeleteYnFalse() {
        return lotMaster.deleteYn.isFalse();
    }

    private BooleanExpression isCodeContain(String code) {
        return lotMaster.lotNo.contains(code);
    }
}
