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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "ITEM", nullable = false)
    private Item item;      // 품목

    @Column(name = "BOM_AMOUNT", nullable = false)
    private int bomAmount;      // BOM수량

    @OneToOne @JoinColumn(name = "WORK_PROCESS", nullable = false)
    private WorkProcess workProcess;        // 투입공정

    @Column(name = "RESERVATION_AMOUNT", nullable = false)
    private int reservationAmount;          // 예약수량

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "UNIT")
    private Unit Unit;                       // 오더단위

    @Column(name = "INPUT_AMOUNT", nullable = false)
    private int inputAmount;                // 투입수량

    @Column(name = "NOTE")
    private String note;                    // 비고

    @Column(name = "SEQ", nullable = false)
    private int seq;                        // seq

    @Column(name = "LEVEL", nullable = false)
    private int level;                      // level

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
