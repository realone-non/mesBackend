package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.custom.ProduceOrderRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ProduceOrderRepositoryImpl implements ProduceOrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    final QProduceOrder produceOrder = QProduceOrder.produceOrder;
    final QBomMaster bomMaster = QBomMaster.bomMaster;
    final QBomItemDetail bomItemDetail = QBomItemDetail.bomItemDetail;
    final QItem item = QItem.item;
    final QWorkProcess workProcess = QWorkProcess.workProcess;
    final QUnit unit = QUnit.unit;
    final QItemAccount itemAccount = QItemAccount.itemAccount;

    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    @Override
    @Transactional(readOnly = true)
    public List<ProduceOrder> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            OrderState orderState,
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
                        isInstructionStatus(orderState),
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
    @Transactional(readOnly = true)
    public List<ProduceOrderDetailResponse> findAllProduceOrderDetail(Long itemId) {
        return jpaQueryFactory
                .select(
                        Projections.fields(ProduceOrderDetailResponse.class,
                                bomItemDetail.id.as("bomItemDetailId"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                itemAccount.account.as("itemAccount"),
                                bomItemDetail.amount.as("bomAmount"),
                                workProcess.workProcessName.as("workProcess"),
                                unit.unitCodeName.as("orderUnit"),
                                bomItemDetail.level.as("level")
                        )
                )
                .from(bomItemDetail)
                .leftJoin(bomMaster).on(bomMaster.id.eq(bomItemDetail.bomMaster.id))
                .leftJoin(item).on(item.id.eq(bomItemDetail.item.id))
                .leftJoin(workProcess).on(workProcess.id.eq(bomItemDetail.workProcess.id))
                .leftJoin(unit).on(unit.id.eq(item.unit.id))
                .leftJoin(itemAccount).on(itemAccount.id.eq(item.itemAccount.id))
                .where(
                        bomMaster.item.id.eq(itemId)
                )
                .fetch();
    }
    @Transactional(readOnly = true)
    public ProduceOrder findByIdforShortage(Long id){
        return jpaQueryFactory
                .selectFrom(produceOrder)
                .where(
                        produceOrder.id.eq(id),
                        produceOrder.deleteYn.eq(false)
                )
                .fetchOne();
    }

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
    private BooleanExpression isInstructionStatus(OrderState orderState) {
        return orderState != null ? produceOrder.orderState.eq(orderState) : null;
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
