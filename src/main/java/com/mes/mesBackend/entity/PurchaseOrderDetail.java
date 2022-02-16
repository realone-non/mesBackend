//package com.mes.mesBackend.entity;
//
//
//import com.mes.mesBackend.entity.enumeration.TestType;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//
///*
// * 9-2. 구매발주 등록 세부내역
// * 요청번호 (1366)
// * 품번 (CF00-00288-01)
// * 품명 (CASE)
// * 규격 (AL / 황색크로메이트)
// * 제조사 품번 (015BTC001A)
// * 발주단위 (개)
// * 발수수량 (16)
// * 단가 (742000)
// * 발구금액 (11872000)
// * 발주금액(원화) (11872000)
// * VAT (1187200)
// * 발주가능수량 (16)
// * 입고수량  (0)
// * 취소수량 (0)
// * 납기일자 (2021.8.10)
// * 비고
// * 수입검사유형 (자동검사)
// * 제조사
// * */
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity(name = "PURCHASE_ORDER_DETAILS")
// TODO: 삭제
//@Data
//public class PurchaseOrderDetail extends BaseTimeEntity {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매발주 세부정보 고유아이디'")
//    private Long id;            // 구매발주 세부정보 고유아이디
//
//    @Column(name = "REQUEST_NO", columnDefinition = "varchar(255) COMMENT '요청번호'")
//    private String requestNo;       // 요청번호
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
//    private Item item;              // 품목
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENt '발주단위'")
//    private Unit orderUnit;                  // 발주단위
//
//    @Column(name = "ORDER_AMOUNT", columnDefinition = "int COMMENT '발주수량'")
//    private int orderAmount;        // 발수수량
//
//    @Column(name = "PRICE", columnDefinition = "int COMMENT '단가'")
//    private int price;              // 단가
//
//    @Column(name = "ORDER_PRICE", columnDefinition = "int COMMENT '발주금액'")
//    private int orderPrice;         // 발주금액
//
//    @Column(name = "VAT", columnDefinition = "varchar(255) COMMENT 'VAT'")
//    private String vat;             // VAT
//
//    @Column(name = "ORDER_POSSIBLE_AMOUNT", columnDefinition = "int COMMENT '발주가능수량'")
//    private int orderPossibleAmount;        // 발주가능수량
//
//    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '입고가능수량'")
//    private int inputAmount;                // 입고수량
//
//    @Column(name = "CANCEL_AMOUNT", columnDefinition = "int COMMENT '취소수량'")
//    private int cancelAmount;       // 취소수량
//
//    @Column(name = "PERIOD_DATE", columnDefinition = "date COMMENT '납기일자'")
//    private LocalDate periodDate;       // 납기일자
//
//    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
//    private String note;            // 비고
//
//    // enum으로 대체
//    @Enumerated(EnumType.STRING)
//    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '수입검사유형'")
//    private TestType testType = TestType.NO_TEST;      // 수입검사유형
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PURCHASE_ORDER")
//    private PurchaseOrder purchaseOrder;        // 구매 발주 등록
//}
