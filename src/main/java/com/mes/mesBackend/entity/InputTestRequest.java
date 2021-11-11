package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.InputTestState;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * 14. 부품수입검사
 * 수입검사의뢰
 * 검색: 공장,창고,LOT 유형,품목,검사유형,품목그룹,요청유형,의뢰기간
 * LOT 번호 (IA8BV000000001)                                      -> 미구현
 * 입고번호 (4175)                                                 -> PurchaseCurrent
 * 품번 (CF00-00224-02)                                          -> PurchaseCurrent
 * 품명 (DIVIDER [AL / 황색크로메이트])                              -> PurchaseCurrent
 * 제조사 품번 (C-UC2-L005F-2)                                     -> PurchaseCurrent
 * 고객사 품번 (UC2-L005F NC2-A005K NC2-A005E NC2-A005B)           -> PurchaseCurrent
 * 제조사 (경일정밀)                                                -> PurchaseCurrent
 * 고객사 (에어로매스터)                                             -> Client
 * 창고 (본사 자재 창고)                                             -> WareHouse
 * 품목형태 (기구)                                                  -> PurchaseCurrent
 * 검사방법 ()                                                     -> TestProcess
 * 검사기준 ()                                                     -> TestCriteria
 * 긴급여부 (Unchecked,checked)
 * 검사성적서 (Unchecked,checked)
 * COC (Unchecked,checked)
 * 요청일시 (2021.9.1  10:11:36)
 * 요청유형 (자동검사)
 * 요청수량 (7)
 * 검사수량 (7)
 * 검사 타입 (의뢰, 진행, 완료)            -> InputTestState
 * 외주여부
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "INPUT_TEST_REQUESTS")
@Data
public class InputTestRequest extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부품수입검사 고유아이디'")
    private Long id;

    @Column(name = "LOT_NO", nullable = false, columnDefinition = "varchar(255) COMMENT 'LOT 번호'")
    private String lotNo;           // LOT번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_CURRENT", columnDefinition = "bigint COMMENT '구매현황'")
    private PurchaseCurrent purchaseCurrent;        // 구매현황

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'")
    private Client client;      // 거래처

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;        // 창고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_PROCESS", columnDefinition = "bigint COMMENT '검사방법'")
    private TestProcess testProcess;        // 검사방법

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_CRITERIA", columnDefinition = "bigint COMMENT '검사기준'")
    private TestCriteria testCriteria;      // 검사기준

    @Column(name = "EMERGENCY_YN", columnDefinition = "bit(1) COMMENT '긴급여부'")
    private boolean emergencyYn;        // 긴급여부

    @Column(name = "TEST_REPORT_YN",  columnDefinition = "bit(1) COMMENT '검사성적서 여부'")
    private boolean testReportYn;       // 검사성적서 여부

    @Column(name = "COC", columnDefinition = "bit(1) COMMENT 'COC 여부'")
    private boolean coc;                // coc 여부

    @Column(name = "REQUEST_DATE_TIME", columnDefinition = "datetime(6) COMMENT '요청일시'")
    private LocalDateTime requestDateTime;      // 요청일시

    @Enumerated(EnumType.STRING)
    @Column(name = "REQUEST_TYPE", columnDefinition = "varchar(255) COMMENT '요청유형'")
    private TestType testType = TestType.NO_TEST;    // 요청유형

    @Column(name = "REQUEST_AMOUNT", columnDefinition = "int COMMENT '요청수량'")
    private int requestAmount;                      // 요청수량

    @Column(name = "TEST_AMOUNT", columnDefinition = "int COMMENT '검사수량'")
    private int testAmount;     // 검사수량

    @Enumerated(EnumType.STRING)
    @Column(name = "INPUT_TEST_STATE", columnDefinition = "varchar(255) COMMENT '부품수입검사 상태값'")
    private InputTestState inputTestState;      // 수입검사의뢰 상태값

    @Column(name = "OUT_SOURCING_YN", columnDefinition = "bit(1) COMMENT '외주여부'")
    private boolean outSourcingYn;              // 외주여부
}
