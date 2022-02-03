package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.entity.QEquipment;
import com.mes.mesBackend.entity.QWorkProcess;
import com.mes.mesBackend.repository.custom.EquipmentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QEquipment equipment = QEquipment.equipment;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

    // 작업공정에 따라 설비 조회
    @Override
    public List<PopEquipmentResponse> findPopEquipmentResponseByWorkProcess(Long workProcessId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                PopEquipmentResponse.class,
                                equipment.id.as("id"),
                                equipment.equipmentCode.as("equipmentCode"),
                                equipment.equipmentName.as("equipmentName"),
                                equipment.workProcess.id.as("workProcessId"),
                                equipment.workProcess.workProcessName.as("workProcessName")
                        )
                )
                .from(equipment)
                .leftJoin(workProcess).on(workProcess.id.eq(equipment.workProcess.id))
                .where(
                        equipment.deleteYn.isFalse(),
                        workProcess.id.eq(workProcessId)
                )
                .fetch();
    }
}
