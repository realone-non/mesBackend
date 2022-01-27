package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ItemAccountCodeResponse;
import com.mes.mesBackend.entity.QItemAccount;
import com.mes.mesBackend.entity.QItemAccountCode;
import com.mes.mesBackend.repository.custom.ItemAccountCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ItemAccountCodeRepositoryImpl implements ItemAccountCodeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QItemAccountCode itemAccountCode = QItemAccountCode.itemAccountCode;
    final QItemAccount itemAccount = QItemAccount.itemAccount;

    @Override
    public List<ItemAccountCodeResponse> findItemAccountCodeResponseByItemAccountId(Long itemAccountId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                ItemAccountCodeResponse.class,
                                itemAccountCode.id.as("itemAccountCodeId"),
                                itemAccountCode.itemAccount.id.as("itemAccountId"),
                                itemAccountCode.detail.as("detail"),
                                itemAccountCode.code.as("code")
                        )
                )
                .from(itemAccountCode)
                .leftJoin(itemAccount).on(itemAccount.id.eq(itemAccountCode.itemAccount.id))
                .where(
                        isItemAccountIdEq(itemAccountId),
                        itemAccountCode.deleteYn.isFalse(),
                        itemAccount.deleteYn.isFalse()
                )
                .fetch();
    }

    private BooleanExpression isItemAccountIdEq(Long itemAccountId) {
        return itemAccountId != null ? itemAccount.id.eq(itemAccountId) : null;
    }
}
