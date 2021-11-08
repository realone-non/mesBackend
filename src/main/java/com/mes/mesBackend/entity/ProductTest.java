package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * 16-1. 검사의뢰 등록
 * 검색: 창고,LOT유형,품목,검사유형,품목그룹,요청유형,의뢰기간
 * 선택 (Unchecked, checked)
 * 검사유형 (공정검사,수입재검사,불량품검사,출하검사)
 * LOT번호 (CIA6DH000000002)
 * 지시번호 (2105100004-0003)
 * 품번 (DN00-MH0-040-21)
 * 품명 (EMI FILTER [WF-05A-24])
 * 제조사 품번 (BT101-BN-J00 (Motherboard))
 * 거래처 (엘아이지정밀기술(주))
 * 창고 (본사 자재 창고)
 * 품목형태 (부자재)
 * 요청유형 (자동검사)
 * 요청일시 (2021.7.5  18:01:56)
 * 검사완료요청일 (2021.7.5)
 * 요청수량 (16)
 * 검사수량 (0)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PRODUCT_TESTS")
@Data
public class ProductTest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제품검사 고유아이디'")
    private Long id;            // 제품검사 고유아이디

    @Column(name = "CHECK_YN", columnDefinition = "bit(1) COMMENT '선택'")
    private boolean checkYn;        // 선택

    // enum으로ㅓ 대체
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_CATEGORY", columnDefinition = "varchar(255) COMMENT '검사유형'")
    private TestCategory testCategory = TestCategory.INPUT_TEST;      // 검사유형

    @Column(name = "LOT_NO", columnDefinition = "varchar(255) COMMENT '검사유형'")
    private String lotNo;       // LOT 번호

    @Column(name = "ORDER_NO", columnDefinition = "varchar(255) COMMENT '지시번호'")
    private String orderNo;     // 지시번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;          // 품목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;        // 창고

    // enum으로 대체
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '요청유형'")
    private TestType testType = TestType.NO_TEST;          // 요청유형

    @Column(name = "REQUEST_DATE_TIME", columnDefinition = "datetime(6) COMMENT '요청일시'")
    private LocalDateTime requestDateTime;      // 요청일시

    @Column(name = "TEST_COMPLETED_DATE", columnDefinition = "date COMMENT '검사완료요청일'")
    private LocalDate testCompletedDate;        // 검사완료요청일

    @Column(name = "REQUEST_AMOUNT", columnDefinition = "int COMMENT '요청수량'")
    private int requestAmount;                  // 요청수량

    @Column(name = "TEST_AMOUNT", columnDefinition = "int COMMENT '검사수량'")
    private int testAmount;                     // 검사수량

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_TEST_STATE", columnDefinition = "varchar(255) COMMENT '검사의뢰 등록 상태값'")
    private ProductTestState productTestState;      // 검사의뢰 등록 상태값
}
