package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;

/*
 * BOM품목 정보 등록
 * 검색: 품목계정(체크),품목그룹(체크),품목(검색창),하위품목(검색창)
 * 레벨 (1)
 * 품번 (CF00-00001-01)
 * 품명 (EMI FILTER [AF2-E001DB])
 * 제조사 (경일정밀)
 * 제조사품번 (C-AV2-E001DB-2)
 * 단위 (개,그램)                -> Unit
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'BOM 품목 정보 등록 고유아이디'")
    private Long id;

    @Column(name = "LEVEL", columnDefinition = "int COMMENT '레벨'")
    private int level;     // 레벨

    @Column(name = "DETAIL_ITEM_NO", nullable = false, columnDefinition = "varchar(255) COMMENT '품번'")
    private String detailItemNo;    // 품번

    @Column(name = "DETAIL_ITEM_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '품명'")
    private String detailItemName;  // 품명

    @Column(name = "MANUFACTURER_ITEM_NO", columnDefinition = "varchar(255) COMMENT '제조사 품번'")
    private String manufacturerItemNo;      //  제조사 품번

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENT '단위'")
    private Unit unit;    // 단위

    @Column(name = "AMOUNT", columnDefinition = "int COMMENT '수량'")
    private int amount;     // 수량

    @Column(name = "TO_BUY", columnDefinition = "varchar(255) COMMENT '구매처'")
    private String toBuy;       // 구매처

    @Column(name = "LOCATION", columnDefinition = "varchar(255) COMMENT '위치'")
    private String location;    // 위치

    @Column(name = "UNIT_PRICE", columnDefinition = "int COMMENT '단가'")
    private int unitPrice;      // 단가

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ACCOUNTS", columnDefinition = "bigint COMMENT '품목계정'")
    private ItemAccount itemAccount;    // 품목계정

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '공정'")
    private WorkProcess workProcess;     // 공정

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOM_MASTERS_ID", columnDefinition = "bigint COMMENT 'BomMaster'")
    private BomMaster bomMaster;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
