package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.entity.QProduceOrder;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.repository.custom.WorkOrderDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class WorkOrderDetailRepositoryImpl implements WorkOrderDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;

    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 작업공정 id, 착수예정일 fromDate~endDate, 지시상태
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderProduceOrderResponse> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            InstructionStatus instructionStatus
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(WorkOrderProduceOrderResponse.class,
                                produceOrder.id.as("id"),
                                produceOrder.produceOrderNo.as("produceOrderNo"),
                                produceOrder.contractItem.item.itemNo.as("itemNo"),
                                produceOrder.contractItem.item.itemName.as("itemName"),
                                produceOrder.expectedStartedDate.as("expectedStartedDate"),
                                produceOrder.contractItem.amount.as("orderAmount"),
                                produceOrder.contractItem.item.unit.unitCodeName.as("unitCodeName"),
                                produceOrder.instructionStatus.as("instructionStatus"),
                                produceOrder.contractItem.contractType.as("contractType"),
                                produceOrder.contract.client.clientName.as("contractClient"),
                                produceOrder.contract.contractNo.as("contractNo"),
                                produceOrder.contract.periodDate.as("periodDate"),
                                produceOrder.contract.note.as("note")
                                )
                )
                .from(produceOrder)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isContractNoContain(contractNo),
                        isProduceOrderNoContain(produceOrderNo),
                        isExpectedCompletedDateBetween(fromDate, toDate),
                        isInstructionStatusEq(instructionStatus)
                )
                .fetch();
    }

    // 품목그룹
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? produceOrder.contractItem.item.itemGroup.id.eq(itemGroupId) : null;
    }

    // 품명|품명
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? produceOrder.contractItem.item.itemNo.contains(itemNoAndName)
                .or(produceOrder.contractItem.item.itemName.contains(itemNoAndName)) : null;
    }

    // 수주번호
    private BooleanExpression isContractNoContain(String contractNo) {
        return contractNo != null ? produceOrder.contract.contractNo.contains(contractNo) : null;
    }

    // 제조오더번호
    private BooleanExpression isProduceOrderNoContain(String produceOrderNo) {
        return produceOrderNo != null ? produceOrder.produceOrderNo.contains(produceOrderNo) : null;
    }

    // 착수예정일 fromDate~toDate
    private BooleanExpression isExpectedCompletedDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? produceOrder.expectedStartedDate.between(fromDate, toDate) :  null;
    }
    // 지시상태
    private BooleanExpression isInstructionStatusEq(InstructionStatus instructionStatus) {
        return instructionStatus != null ? produceOrder.instructionStatus.eq(instructionStatus) : null;
    }
}
