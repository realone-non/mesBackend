package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* 9-6. 구매입고 반품등록
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PURCHASE_RETURNS")
@Data
public class PurchaseReturn extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매입고 반품등록 고유아이디'")
    private Long id;

    //구매입고정보
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "PURCHASE_INPUT", columnDefinition = "bigint COMMENT '구매입고정보'", nullable = false)
    private PurchaseInput purchaseInput;

    // 반품일시
    @Column(name = "RETURN_DATE", columnDefinition = "datetime(6) COMMENT '반품일시'", nullable = false)
    private LocalDateTime returnDate;

    // 반품수량 0
    @Column(name = "RETURN_AMOUNT", columnDefinition = "int COMMENT '반품수량'", nullable = false)
    private int returnAmount;

    // 비고
    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;
}
