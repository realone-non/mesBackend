package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 제조오더 세부내역
 * 품번                    -> Item
 * 품명                    -> Item
 * 품목계정                  -> Item
 * BOM수량
 * 투입공정                  -> WorkProcesses
 * 예약수량
 * 오더단위                  -> Unit
 * 투입수량
 * 비고
 * SEQ
 * 레벨
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PRODUCE_ORDER_DETAIL")
@Data
public class ProduceOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제조오더 세부내역 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", nullable = false, columnDefinition = "bigint COMMENT '품목'")
    private Item item;      // 품목

    @Column(name = "BOM_AMOUNT", nullable = false, columnDefinition = "int COMMENT 'BOM수량'")
    private int bomAmount;      // BOM수량

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS", nullable = false, columnDefinition = "bigint COMMENT '공정'")
    private WorkProcess workProcess;        // 투입공정

    @Column(name = "RESERVATION_AMOUNT", nullable = false, columnDefinition = "int COMMENT '에약수량'")
    private int reservationAmount;          // 예약수량

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENT '단위'")
    private Unit Unit;                       // 오더단위

    @Column(name = "INPUT_AMOUNT", nullable = false, columnDefinition = "int COMMENT '투입수량'")
    private int inputAmount;                // 투입수량

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                    // 비고

    @Column(name = "SEQ", nullable = false, columnDefinition = "int COMMENT 'seq'")
    private int seq;                        // seq

    @Column(name = "LEVEL", nullable = false, columnDefinition = "int COMMENT 'level'")
    private int level;                      // level

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
