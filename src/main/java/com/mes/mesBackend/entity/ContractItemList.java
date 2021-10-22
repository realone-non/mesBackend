package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
/*
 * 수주 품목 리스트
 * 품번 (AA01-MC2-E003B)       ->Item
 * 품명 (EMI FILTER)           -> Item
 * 규격 (MC2-E003B)            -> Item
 * 수주단위 (개)                  -> Unit
 * 수주수량 (10)
 * 단가 (1960000)
 * 수주금액 (19600000)
 * 수주금액(원화) (19600000)
 * 부가세 (1960000)
 * 수주유형 (방산)
 * 납기일자 (2021.10.5)
 * 고객발주번호 (AD-P2-210712-004)     -> Contract
 * 규격화 품번 ()
 * 비고
 * 첨부파일 (2 Files)       -> 미구현
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CONTRACT_ITEM_LISTS")
@Data
public class ContractItemList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수주 품목 리스트 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", nullable = false, columnDefinition = "bigint COMMENT '품목'")
    private Item item;      // 품목

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", nullable = false, columnDefinition = "bigint COMMENT '수주단위'")
    private Unit unit;      // 수주단위

    @Column(name = "AMOUNT", nullable = false, columnDefinition = "bigint COMMENT '수주수량'")
    private int amount;     // 수주수량

    @Column(name = "UNIT_PRICE", nullable = false, columnDefinition = "bigint COMMENT '단가'")
    private Long unitPrice;     // 단가

    @Column(name = "CONTRACT_PRICE", columnDefinition = "bigint COMMENT '수주금액'")
    private Long contractPrice; // 수주금액

    @Column(name = "CONTRACT_PRICE_WON", columnDefinition = "bigint COMMENT '수주금액(원화)'")
    private Long contractPriceWon;      // 수주금액(원화)

    @Column(name = "SURTAX", columnDefinition = "bigint COMMENT '부가세'")
    private Long surtax;            // 부가세

    @Column(name = "CONTRACT_TYPE", columnDefinition = "bigint COMMENT '수주유형'")
    private String contractType;     // 수주유형

    @Column(name = "PERIOD_DATE", columnDefinition = "bigint COMMENT '납기일자'")
    private LocalDate periodDate;       // 납기일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT", nullable = false, columnDefinition = "bigint COMMENT '수주'")
    private Contract contract;      // 수주

    @Column(name = "STANDART_ITEM_NO", columnDefinition = "bigint COMMENT '규격화 품번'")
    private String standardItemNo;      // 규격화 품번

    @Column(name = "NOTE", columnDefinition = "bigint COMMENT '비고'")
    private String note;        // 비고

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
