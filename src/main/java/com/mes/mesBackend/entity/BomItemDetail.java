package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * BOM품목 정보
 * 레벨, 품번, 품며으 제조사, 제조사품번, 단위, 수량, 구매처, 위치, 단가, 금액, 품목계정, 공정, 사용, 비고
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

    @Column(name = "AMOUNT")
    private int amount;     // 수량

    // 일대다 단방향
    @OneToOne @JoinColumn(name = "ITEM_ACCOUNTS_ID")
    private ItemAccount itemAccount;    // 품목계정

    @Column(name = "PROCESS")
    private String process;     // 공정

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;      // 사용

    @Column(name = "NOTE")
    private String note;        // 비고

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOM_MASTERS_ID")
    private BomMaster bomMaster;
}
