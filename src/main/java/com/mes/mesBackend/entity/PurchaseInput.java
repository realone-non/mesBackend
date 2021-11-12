package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* 9-5. 구매입고 등록
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PURCHASE_INPUTS")
@Data
public class PurchaseInput extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매입고 고유아이디'")
    private Long id;

    // 거래처, 거래처명
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'", nullable = false)
    private Client client;

    // 입고수량     default 0
    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '입고수량'", nullable = false)
    private int inputAmount;

    // 입고창고
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '입고창고'", nullable = false)
    private WareHouse wareHouse;

    // 미입고수량 0
    @Column(name = "ALREADY_INPUT", columnDefinition = "int COMMENT '미입고수량'", nullable = false)
    private int alreadyInput;

    // 화폐
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CURRENCY", columnDefinition = "bigint COMMENT '화폐'", nullable = false)
    private Currency currency;

    // 비고
    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    // 일대일  구매요청
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "PURCHASE_REQUEST", columnDefinition = "bigint COMMENT '구매요청'", nullable = false)
    private PurchaseRequest purchaseRequest;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;
}
