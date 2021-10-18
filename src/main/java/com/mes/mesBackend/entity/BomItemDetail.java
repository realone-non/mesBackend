package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * BOM품목 정보 등록
 * 레벨 (1)
 * 품번 (CF00-00001-01)
 * 품명 (EMI FILTER [AF2-E001DB])
 * 제조사 (경일정밀)
 * 제조사품번 (C-AV2-E001DB-2)
 * 단위 (개,그램)
 * 수량 (1,0.1)
 * 구매처 ()
 * 위치 ()
 * 단가 (13000,10)
 * 금액 (단가*수량)
 * 품목계정 (원재료, 부재료)
 * 공정 ()
 * 사용,
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BOM_ITEM_DETAILS")
@Data
public class BomItemDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "LEVEL")
    private Long level;     // 레벨

    @Column(name = "DETAIL_ITEM_NO", nullable = false)
    private String detailItemNo;    // 품번

    @Column(name = "DETAIL_ITEM_NAME", nullable = false)
    private String detailItemName;  // 품명

    @Column(name = "MANUFACTURER_ITEM_NO")
    private String manufacturerItemNo;      //  제조사 품번

    @Column(name = "UNIT", nullable = false)
    private String unit;    // 단위

    @Column(name = "AMOUNT")
    private int amount;     // 수량

    @Column(name = "TO_BUY")
    private String toBuy;       // 구매처

    @Column(name = "LOCATION")
    private String location;    // 위치

    @Column(name = "UNIT_PRICE")
    private int unitPrice;      // 단가

    // 일대다 단방향
    @OneToOne @JoinColumn(name = "ITEM_ACCOUNTS_ID")
    private ItemAccount itemAccount;    // 품목계정

    // 일대다 단방향
    @OneToOne @JoinColumn(name = "WORK_PROCESS_ID")
    private WorkProcess workProcess;     // 공정

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;      // 사용

    @Column(name = "NOTE")
    private String note;        // 비고

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOM_MASTERS_ID")
    private BomMaster bomMaster;
}
