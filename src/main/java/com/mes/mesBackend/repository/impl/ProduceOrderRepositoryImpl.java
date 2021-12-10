package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.QBomItemDetail;
import com.mes.mesBackend.entity.QBomMaster;
import com.mes.mesBackend.entity.QProduceOrder;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;
import com.mes.mesBackend.repository.custom.ProduceOrderRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ProduceOrderRepositoryImpl implements ProduceOrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QBomMaster bomMaster = QBomMaster.bomMaster;
    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;


    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    @Override
    public List<ProduceOrder> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            InstructionStatus instructionStatus,
            String produceOrderNo,
            String contractNo,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return jpaQueryFactory
                .selectFrom(produceOrder)
                .where(
                        isItemGroupEq(itemGroupId),
                        isItemNoAndItemNameContain(itemNoAndName),
                        isInstructionStatus(instructionStatus),
                        isProduceOrderNoContain(produceOrderNo),
                        isContractNoContain(contractNo),
                        isExpectedCompletedDateBetween(fromDate, toDate),
                        isDeleteYnFalse()
                )
                .fetch();
    }


    // where: produceOrder.contractItem.item = bomMaster.item
    // produceOrder.contractItem.item = bomMaster.item 랑 같은걸 찾으면 ? list로
    @Override
    public List<ProduceOrderDetailResponse> findAllProduceOrderDetail(Long produceOrderId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(ProduceOrderDetailResponse.class,
                                bomItemDetail.item.itemNo.as("itemNo"),
                                bomItemDetail.item.itemName.as("itemName"),
                                bomItemDetail.item.itemAccount.account.as("itemAccount"),
                                bomItemDetail.amount.as("bomAmount"),
                                bomItemDetail.workProcess.workProcessName.as("workProcess"),
                                bomItemDetail.amount.multiply(produceOrder.contractItem.amount).as("reservationAmount"),
                                bomItemDetail.unit.unitCodeName.as("orderUnit")
                        )
                )
                .from(produceOrder)
                .innerJoin(produceOrder.contractItem.item, bomMaster.item)
                .where(
                        isProduceOrderId(produceOrderId),
                        ddd()
                )
                .fetch();
    }

    // bomItemDetail 에서 BomMaster랑 같은걸 찾음.
    private BooleanExpression ddd() {
        return bomItemDetail.bomMaster.id.eq(bomMaster.item.id);
    }

    // produceOrederId 와 같은 row를 찾음.
    private BooleanExpression isProduceOrderId(Long produceOrderId) {
        return produceOrder.id.eq(produceOrderId);
    }

    // 품목그룹 id
    /*
    * > contract.item.itemGroup -> NullPointerException
    * QueryPath 를 이용해서 쿼리를 빌드할 때 QueryDSL은 기본적으로 현재 엔티티의 속성값에 대해서만 참조 가능.
    * 즉, Direct Properties 만 BuildPath 에 포함할 수 있으며 Deep Initialize 가 필요한 부분은
    * @QueryInit 어노테이션을 적용해야함.
    * */
    private BooleanExpression isItemGroupEq(Long itemGroupId) {
        return itemGroupId != null ? produceOrder.contractItem.item.itemGroup.id.eq(itemGroupId) : null;
    }
    // 품명|품번
    private BooleanExpression isItemNoAndItemNameContain(String itemNoAndName) {
        return itemNoAndName != null ? produceOrder.contractItem.item.itemNo.contains(itemNoAndName)
                .or(produceOrder.contractItem.item.itemName.contains(itemNoAndName)) : null;
    }
    // 지시상태
    private BooleanExpression isInstructionStatus(InstructionStatus instructionStatus) {
        return instructionStatus != null ? produceOrder.instructionStatus.eq(instructionStatus) : null;
    }
    // 제조오더 번호
    private BooleanExpression isProduceOrderNoContain(String produceOrderNo) {
        return produceOrderNo != null ? produceOrder.produceOrderNo.contains(produceOrderNo) : null;
    }
    // 수주번호
    private BooleanExpression isContractNoContain(String contractNo) {
        return contractNo != null ? produceOrder.contract.contractNo.contains(contractNo) : null;
    }
    // 착수예정일 fromDate~toDate
    private BooleanExpression isExpectedCompletedDateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null ? produceOrder.expectedStartedDate.between(fromDate, toDate) :  null;
    }
    // 삭제여부
    private BooleanExpression isDeleteYnFalse() {
        return produceOrder.deleteYn.isFalse();
    }
}
