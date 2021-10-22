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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제조오더등록 고유아이디'")
    private Long id;

    @Column(name = "PRODUCE_ORDER_NO", nullable = false, unique = true, columnDefinition = "bigint COMMENT '제조오더번호'")
    private String produceOrderNo;      // 제조오더번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT", nullable = false, columnDefinition = "bigint COMMENT '수주'")
    private Contract contract;          // 수주

    @Column(name = "START_DATE", columnDefinition = "bigint COMMENT '칙수예정일'")
    private LocalDate startDate;        // 착수예정일

    @Column(name = "END_DATE", columnDefinition = "bigint COMMENT '완료예정일'")
    private LocalDate endDate;          // 완료예정일

    @Column(name = "CONTRACT_AMOUNT", columnDefinition = "bigint COMMENT '수주수량'")
    private int contractAmount;         // 수주수량

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTRUCTION_STATUS", columnDefinition = "bigint COMMENT '지시상태'")
    private InstructionStatus instructionStatus;    // 지시상태

    @Column(name = "PRODUCE_TYPE", columnDefinition = "bigint COMMENT '수주유형'")
    private String produceType;         // 수주유형

    @Column(name = "RATE", columnDefinition = "bigint COMMENT '보정율'")
    private int rate;                   // 보정율

    @Column(name = "NOTE", columnDefinition = "bigint COMMENT '비고'")
    private String note;                // 비고

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
