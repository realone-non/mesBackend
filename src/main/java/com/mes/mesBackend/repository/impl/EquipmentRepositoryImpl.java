package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.QEquipment;
import com.mes.mesBackend.entity.QWorkProcess;
import com.mes.mesBackend.repository.custom.EquipmentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QEquipment equipment = QEquipment.equipment;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    // 작업공정에 따라 설비 조회
    @Override
    public List<PopEquipmentResponse> findPopEquipmentResponseByWorkProcess(Long workProcessId, Boolean produceYn) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopEquipmentResponse.class,
                                equipment.id.as("id"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                equipment.workProcess.id.as("workProcessId"),
                                equipment.workProcess.workProcessName.as("workProcessName"),
                                equipment.produceYn.as("produceYn")
                        )
                )
                .from(equipment)
                .leftJoin(workProcess).on(workProcess.id.eq(equipment.workProcess.id))
                .where(
                        equipment.deleteYn.isFalse(),
                        workProcess.id.eq(workProcessId),
                        isProduceYnEq(produceYn)
                )
                .fetch();
    }

    private BooleanExpression isProduceYnEq(Boolean produceYn) {
        if (produceYn != null) {
            if (produceYn) {
                return equipment.produceYn.isTrue();
            } else {
                return equipment.produceYn.isFalse();
            }
        } else
            return null;
    }

    //공정으로 설비 찾기
    @Transactional(readOnly = true)
    public Equipment findByWorkProcess(Long workProcessId){
        return jpaQueryFactory
                .selectFrom(equipment)
                .where(
                        equipment.workProcess.id.eq(workProcessId),
                        equipment.deleteYn.eq(false),
                        equipment.useYn.eq(true)
                )
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<Equipment> findByCondition(String equipmentName, Integer checkCycle) {
        return  jpaQueryFactory
                .selectFrom(equipment)
                .where(
                        isEquipmentNameContain(equipmentName),
                        isCheckCycleEq(checkCycle),
                        equipment.deleteYn.isFalse()
                )
                .orderBy(equipment.workProcess.orders.asc(), equipment.createdDate.desc())
                .fetch();
    }
    private BooleanExpression isEquipmentNameContain(String equipmentName) {
        return equipmentName != null ? equipment.equipmentName.contains(equipmentName) : null;
    }

    private BooleanExpression isCheckCycleEq(Integer checkCycle) {
        return checkCycle != null ? equipment.checkCycle.eq(checkCycle) : null;
    }
}
