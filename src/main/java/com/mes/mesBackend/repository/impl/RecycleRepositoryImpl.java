package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.dto.response.RecycleResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.RecycleRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecycleRepositoryImpl implements RecycleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QRecycle recycle = QRecycle.recycle;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    @Transactional(readOnly = true)
    public Optional<RecycleResponse> findByIdAndDeleteYn(long id){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                            Projections.fields(
                                RecycleResponse.class,
                                workProcess.id.as("workProcessId"),
                                recycle.id.as("recycleId"),
                                recycle.recycleName.as("recycleName")
                            )
                    )
                    .from(recycle)
                    .innerJoin(workProcess).on(workProcess.id.eq(recycle.workProcess.id))
                    .where(
                            recycle.id.eq(id),
                            recycle.useYn.eq(true),
                            recycle.deleteYn.eq(false)
                    )
                    .fetchOne()
        );
    }

    @Transactional(readOnly = true)
    public List<RecycleResponse> findRecycles(Long workProcessId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                RecycleResponse.class,
                                workProcess.id.as("workProcessId"),
                                recycle.id.as("recycleId"),
                                recycle.recycleName.as("recycleName")
                        )
                )
                .from(recycle)
                .innerJoin(workProcess).on(workProcess.id.eq(recycle.workProcess.id))
                .where(
                        isWorkProcessNull(workProcessId),
                        recycle.useYn.eq(true),
                        recycle.deleteYn.eq(false)
                )
                .fetch();
    }

    private BooleanExpression isWorkProcessNull(Long workProcessId){
        return workProcessId != null ? workProcess.id.eq(workProcessId) : null;
    }

}
