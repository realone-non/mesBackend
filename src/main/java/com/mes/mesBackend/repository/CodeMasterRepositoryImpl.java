package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.entity.QCodeMaster;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CodeMasterRepositoryImpl implements CodeMasterRepositoryCustom {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QCodeMaster codeMaster = QCodeMaster.codeMaster;
    // 코드 마스터 조회
    public Page<CodeMaster> findByMainCodeAndCodeName(
            String mainCode,
            String codeName,
            Pageable pageable
    ) {
        QueryResults<CodeMaster> results = jpaQueryFactory
                .selectFrom(codeMaster)
                .where(
                        isCodeNameContaining(codeName),
                        isMainCodeContaining(mainCode),
                        isDeleteYnFalse())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 각 조건에 대해 null인지 판별 후 BooleanExpression 리턴
    // mainCode 검색
    private BooleanExpression isMainCodeContaining(String mainCode) {
        return mainCode != null ? codeMaster.mainCode.contains(mainCode).and(isDeleteYnFalse()) : null;
    }

    // codeName 검색
    private BooleanExpression isCodeNameContaining(String codeName) {
        return codeName != null ? codeMaster.codeName.contains(codeName).and(isDeleteYnFalse()) : null;
    }

    // deleteYn false
    private BooleanExpression isDeleteYnFalse() {
        return codeMaster.useYn.isFalse();
    }
}
