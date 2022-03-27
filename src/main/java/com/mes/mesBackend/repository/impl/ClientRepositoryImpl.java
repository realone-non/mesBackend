package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.QClient;
import com.mes.mesBackend.repository.custom.ClientRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QClient client = QClient.client;

    // 거래처 유형, 거래처 코드, 거래처 명
    @Transactional(readOnly = true)
    public List<Client> findByTypeAndCodeAndName(
            Long type,
            String clientCode,
            String name
    ) {
        return jpaQueryFactory
                .selectFrom(client)
                .where(
                        isTypeContaining(type),
                        isCodeContaining(clientCode),
                        isNameContaining(name),
                        isDeleteYnFalse()
                )
                .orderBy(client.createdDate.desc())
                .fetch();
    }

    // 각 조건에 대해 null인지 판별 후 BooleanExpression 리턴
    // 거래처유형에 대한 검색조건
    private BooleanExpression isTypeContaining(Long type) {
        return type != null ? client.clientType.id.eq(type) : null;
    }

    // 거래처 코드에 대한 검색 조건
    private BooleanExpression isCodeContaining(String clientCode) {
        return clientCode != null ? client.clientCode.contains(clientCode) : null;
    }

    // 거래처명에 대한 검색 조건
    private BooleanExpression isNameContaining(String name) {
        return name != null ? client.clientName.contains(name) : null;
    }

    // deleteYn false
    private BooleanExpression isDeleteYnFalse() {
        return client.deleteYn.isFalse();
    }
}
