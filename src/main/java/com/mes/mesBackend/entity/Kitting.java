package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* 11-7. kitting 등록
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "KITTINGS")
@Data
public class Kitting extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'kitting 고유아이디'")
    private Long id;

    // 품번, 제조사품번, 품명, 품목계정
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    // 봄수량
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOM_MASTER", columnDefinition = "bigint COMMENT '봄마스터'", nullable = false)
    private BomMaster bomMaster;

    // 봄품목 정보
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOM_ITEM_DETAIL", columnDefinition = "bigint COMMENT '봄품목'", nullable = false)
    private BomItemDetail bomItemDetail;

    // 키팅수량 0
    @Column(name = "KITTING_AMOUNT", columnDefinition = "int COMMENT 'kitting 수량'", nullable = false)
    private int kittingAmount;

    // 예약수량 0
    @Column(name = "RESERVE_AMOUNT", columnDefinition = "int COMMENT '예약수량'", nullable = false)
    private int reserveAmount;

    // 투입공정
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '투입공정'", nullable = false)
    private WorkProcess workProcess;

    // 재고수량0
    @Column(name = "STOCK_AMOUNT", columnDefinition = "int COMMENT '재고수량'", nullable = false)
    private int stockAmount;

    // 오더수량0
    @Column(name = "ORDER_AMOUNT", columnDefinition = "int COMMENT '오더수량'", nullable = false)
    private int orderAmount;

    // 재사용수량0
    @Column(name = "RECYCLE_AMOUNT", columnDefinition = "int COMMENT '재사용수량'", nullable = false)
    private int recycleAmount;

    // 불량수량0
    @Column(name = "BAD_AMOUNT", columnDefinition = "int COMMENT '불량수량'",nullable = false)
    private int badAmount;

    // 비고
    @Column(name = "note", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
