package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.QClient;
import com.mes.mesBackend.repository.ClientRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepositoryCustom {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QClient client = QClient.client;

    // 거래처 유형, 거래처 코드, 거래처 명
    public Page<Client> findByTypeAndCodeAndName(
            Long type,
            String clientCode,
            String name,
            Pageable pageable
    ) {
        QueryResults<Client> results = jpaQueryFactory
                .selectFrom(client)
                .where(
                        isTypeContaining(type),
                        isCodeContaining(clientCode),
                        isNameContaining(name),
                        isDeleteYnFalse()
                )
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 각 조건에 대해 null인지 판별 후 BooleanExpression 리턴
    // 거래처유형에 대한 검색조건
    private BooleanExpression isTypeContaining(Long type) {
        return type != null ? client.clientType.id.eq(type) : null;
//        return type != null ? client.clientType.id.eq(type).and(isDeleteYnFalse()) : null;
    }

    // 거래처 코드에 대한 검색 조건
    private BooleanExpression isCodeContaining(String clientCode) {
        return clientCode != null ? client.clientCode.contains(clientCode) : null;
    }

    // 거래처명에 대한 검색 조건
    private BooleanExpression isNameContaining(String name) {
        return name != null ? client.name.contains(name) : null;
    }

    // deleteYn false
    private BooleanExpression isDeleteYnFalse() {
        return client.deleteYn.isFalse();
    }
}
