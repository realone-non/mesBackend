package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.OrderState;
import com.querydsl.core.annotations.QueryInit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

/*
 * 6-1. 제조오더 등록
 * 검색: 공장,품목그룹,품목,지시상태,제조오더번호,수주번호,착수예정일,자재납기일자
 * 제조오더번호 (2107080001)
 * 품번 (AA01-JT3-M020BC)                  -> Contract -> ContractItemList -> Item
 * 품명 (EMI FILTER [JT3-M020BC])          -> Contract -> ContractItemList -> Item
 * 수주번호 (20210705-001)                   -> Contract
 * 납기일자 (2021.8.10)                      -> Contract
 * 수주처 (이엔테크놀로지(주))                  -> Contract
 * 착수예정일 (2021.7.19)
 * 완료예정일 (2021.7.26)
 * 수주수량 (20)
 * 지시상태 (예정)                             -> InstructionStatus
 * 수주유형 (방산)
 * 보정율 (0)
 * 비고
 * 제조오더품번리스트: 품번,품명,품목계정,BOM수량,투입공정,예약수량,오더단위,투입수량,비고,SEQ,레벨
 * */

@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "PRODUCE_ORDERS")
@Data
public class ProduceOrder extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제조오더등록 고유아이디'")
    private Long id;

    @Column(name = "PRODUCE_ORDER_NO", unique = true, columnDefinition = "varchar(255) COMMENT '제조오더번호'", nullable = false)
    private String produceOrderNo;      // 제조오더번호

    @QueryInit("*.*")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT", columnDefinition = "bigint COMMENT '수주'", nullable = false)
    private Contract contract;          // 수주

    @QueryInit("*.*")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT_ITEM", columnDefinition = "bigint COMMENT '수주 품목'", nullable = false)
    private ContractItem contractItem;

    @Column(name = "EXPECTED_STARTED_DATE", columnDefinition = "date COMMENT '칙수예정일'", nullable = false)
    private LocalDate expectedStartedDate;        // 착수예정일

    @Column(name = "EXPECTED_COMPLETED_DATE", columnDefinition = "date COMMENT '완료예정일'", nullable = false)
    private LocalDate expectedCompletedDate;          // 완료예정일

    @Enumerated(STRING)
    @Column(name = "ORDER_STATE", columnDefinition = "varchar(255) COMMENT '지시상태'", nullable = false)
    private OrderState orderState = SCHEDULE;    // 지시상태

    @Column(name = "RATE", columnDefinition = "int COMMENT '보정율'", nullable = false)
    private int rate = 0;                   // 보정율

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                // 비고

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void created(Contract contract, ContractItem contractItem) {
        setContract(contract);
        setContractItem(contractItem);
        setOrderState(SCHEDULE);
    }

    public void update(ProduceOrder newProduceOrder, Contract newContract, ContractItem newContractItem) {
        setContract(newContract);
        setContractItem(newContractItem);
        setExpectedStartedDate(newProduceOrder.expectedStartedDate);
        setExpectedCompletedDate(newProduceOrder.expectedCompletedDate);
        setOrderState(newProduceOrder.orderState);
        setRate(newProduceOrder.rate);
        setNote(newProduceOrder.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
