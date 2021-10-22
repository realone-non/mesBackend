package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 11-2. 재고조사 정보
 * 창고                -> StockInspectRequest
 * 품번                -> StockInspectRequest
 * 품명                -> StockInspectRequest
 * 저장위치              -> StockInspectRequest
 * LOT유형             -> 미구현
 * LOT번호             -> 미구현
 * DB수량
 * 실사수량
 * 차이량
 * 승인자
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "STOCK_INSPECT_INFOS")
@Data
public class StockInspectInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재고조사 정보 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_INSPECT_REQUEST", columnDefinition = "bigint COMMENT '재고실사 의뢰 정보'")
    private StockInspectRequest stockInspectRequest;        // 재고실사 의뢰 정보

    @Column(name = "DB_AMOUNT", columnDefinition = "int COMMENT 'DB수량'")
    private int dbAmount;       // DB수량

    @Column(name = "INSPECT_AMOUNT", columnDefinition = "int COMMENT '실사수량'")
    private int inspectAmount;      // 실사수량

    @Column(name = "APPROVAL", columnDefinition = "varchar(255) COMMENT '승인자'")
    private String approval;            // 승인자

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                // 비고
}
