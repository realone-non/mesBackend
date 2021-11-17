package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QWorkCenterCheck;
import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.repository.custom.WorkCenterCheckRepositoryCustom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class WorkCenterCheckRepositoryImpl implements WorkCenterCheckRepositoryCustom {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QWorkCenterCheck workCenterCheck = QWorkCenterCheck.workCenterCheck;

    // 작업장별 검유형 페이징 조회/ 검색: 작업장, 점검유형
    public Page<WorkCenterCheck> findByWorkCenterAndCheckTypes(
            Long workCenterId,
            Long checkTypeId,
            Pageable pageable
    ) {
        QueryResults<WorkCenterCheck> results = jpaQueryFactory
                .selectFrom(workCenterCheck)
                .where(
                        isCheckTypeEq(checkTypeId),
                        isWorkCenterEq(workCenterId),
                        isDeleteYnFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 작업장으로 조회
    private BooleanExpression isWorkCenterEq(Long workCenterId) {
        return workCenterId != null ? workCenterCheck.workCenter.id.eq(workCenterId) : null;
    }

    // 점검유형으로 조회
    private BooleanExpression isCheckTypeEq(Long checkTypeId) {
        return checkTypeId != null ? workCenterCheck.checkType.id.eq(checkTypeId) : null;
    }

    // 삭제여부 false
    private BooleanExpression isDeleteYnFalse() {
        return workCenterCheck.deleteYn.isFalse();
    }
}
