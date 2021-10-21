package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 제조오더 등록
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PRODUCE_ORDERS")
@Data
public class ProduceOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCE_ORDER_NO", nullable = false, unique = true)
    private String produceOrderNo;      // 제조오더번호

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CONTRACT", nullable = false)
    private Contract contract;          // 수주

    @Column(name = "START_DATE")
    private LocalDate startDate;        // 착수예정일

    @Column(name = "END_DATE")
    private LocalDate endDate;          // 완료예정일

    @Column(name = "CONTRACT_AMOUNT")
    private int contractAmount;         // 수주수량

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "INSTRUCTION_STATUS")
    private InstructionStatus instructionStatus;    // 지시상태

    @Column(name = "PRODUCE_TYPE")
    private String produceType;         // 수주유형

    @Column(name = "RATE")
    private int rate;                   // 보정율

    @Column(name = "NOTE")
    private String note;                // 비고

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장
}
