package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 11-1. 재고조사 의뢰 상세 정보
 * 창고유형                  -> WareHouse
 * 창고                     -> WareHouse
 * 품목그룹                  -> Item
 * 품번                     -> Item
 * 품명                     -> Item
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "STOCK_INSPECT_REQUEST_DETAILS")
@Data
public class StockInspectRequestDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재고조사 의뢰 상세 정보 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고유형'")
    private WareHouse wareHouse;        // 창고유형

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;                  // 품목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_INSPECT_REQUEST", columnDefinition = "bigint COMMENT '재고실사 의뢰 정보'")
    private StockInspectRequest stockInspectRequest;        // 재고실사 의뢰 정보 등록
}
