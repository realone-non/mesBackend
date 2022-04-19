//package com.mes.mesBackend.repository.impl;
//
//import com.mes.mesBackend.entity.ModifiedLog;
//import com.mes.mesBackend.entity.QModifiedLog;
//import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
//import com.mes.mesBackend.repository.custom.ModifiedLogRepositoryCustom;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//
//import java.util.Optional;
//
//import static com.mes.mesBackend.entity.enumeration.ModifiedDivision.*;
//
//@RequiredArgsConstructor
//public class ModifiedLogRepositoryImpl implements ModifiedLogRepositoryCustom {
//    private final JPAQueryFactory jpaQueryFactory;
//    private final QModifiedLog modifiedLog = QModifiedLog.modifiedLog;
//
//    // 수정 기록 생성
//    @Override
//    public Optional<ModifiedLog> findByModifiedDivisionId(Long divisionId, ModifiedDivision modifiedDivision) {
//        return Optional.ofNullable(
//                jpaQueryFactory
//                        .select(modifiedLog)
//                        .from(modifiedLog)
//                        .where(
//                                isModifiedDivision(divisionId, modifiedDivision),
//                                modifiedLog.division.isFalse()
//                        )
//                        .orderBy(modifiedLog.date.desc())
//                        .fetchFirst()
//        );
//    }
//
//    // 생성 기록 생성
//    @Override
//    public Optional<ModifiedLog> findByInsertDivisionId(Long divisionId, ModifiedDivision modifiedDivision) {
//        return Optional.ofNullable(
//                jpaQueryFactory
//                        .selectFrom(modifiedLog)
//                        .where(
//                                isModifiedDivision(divisionId, modifiedDivision),
//                                modifiedLog.division.isTrue()
//                        )
//                        .orderBy(modifiedLog.date.desc())
//                        .fetchFirst()
//        );
//    }
//
//    private BooleanExpression isModifiedDivision(Long divisionId, ModifiedDivision modifiedDivision) {
//        if (modifiedDivision.equals(UNIT)) return modifiedLog.unit.id.eq(divisionId);
//        if (modifiedDivision.equals(ITEM_GROUP)) return modifiedLog.itemGroup.id.eq(divisionId);
//        if (modifiedDivision.equals(WORK_PROCESS)) return modifiedLog.workProcess.id.eq(divisionId);
//        if (modifiedDivision.equals(WORK_LINE)) return modifiedLog.workLine.id.eq(divisionId);
//        if (modifiedDivision.equals(WORK_DOCUMENT)) return modifiedLog.workDocument.id.eq(divisionId);
//        if (modifiedDivision.equals(EQUIPMENT_MAINTENANCE)) return modifiedLog.equipmentMaintenance.id.eq(divisionId);
//        if (modifiedDivision.equals(PURCHASE_REQUEST)) return modifiedLog.purchaseRequest.id.eq(divisionId);
//        if (modifiedDivision.equals(WORK_CENTER_CHECK_DETAIL)) return modifiedLog.workCenterCheckDetail.id.eq(divisionId);
//        return null;
//    }
//}
