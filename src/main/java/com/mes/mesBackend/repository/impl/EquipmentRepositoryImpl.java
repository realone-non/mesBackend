package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.QEquipment;
import com.mes.mesBackend.entity.QWorkProcess;
import com.mes.mesBackend.repository.custom.EquipmentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class EquipmentRepositoryImpl implements EquipmentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QEquipment equipment = QEquipment.equipment;
    final QWorkProcess workProcess = QWorkProcess.workProcess;

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
}
