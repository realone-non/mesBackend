package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
* 9-6. 구매입고 반품등록
* */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "PURCHASE_INPUT_RETURNS")
@Data
public class PurchaseInputReturn extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매입고 반품등록 고유아이디'")
    private Long id;

    // 반품일시
    @Column(name = "RETURN_DATE", columnDefinition = "date COMMENT '반품일시'", nullable = false)
    private LocalDate returnDate;

    // 반품수량
    @Column(name = "RETURN_AMOUNT", columnDefinition = "int COMMENT '반품수량'", nullable = false)
    private int returnAmount;

    @Column(name = "RETURN_DIVISION", columnDefinition = "bit(1) COMMENT '반품종류'", nullable = false)
    private boolean returnDivision;

    // 비고
    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'lotMaster'", nullable = false)
    private LotMaster lotMaster;

    public void update(PurchaseInputReturn newPurchaseInputReturn) {
        setReturnDate(newPurchaseInputReturn.returnDate);
        setReturnAmount(newPurchaseInputReturn.returnAmount);
        setNote(newPurchaseInputReturn.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
