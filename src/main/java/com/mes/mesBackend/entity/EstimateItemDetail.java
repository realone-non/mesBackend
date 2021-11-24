package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 품목견적서 세부정보 (견적서가 가지고 있는 품목정보)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "ESTIMATE_ITEM_DETAILS")
@Data
public class EstimateItemDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목견적서 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ESTIMATE", columnDefinition = "bigint COMMENT '견적서'", nullable = false)
    private Estimate estimate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    @Column(name = "AMOUNT", columnDefinition = "int COMMENT '수량'", nullable = false)
    private int amount;

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENt '비고'")
    private String note;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;

    public void addJoin(Estimate estimate, Item item) {
        setEstimate(estimate);
        setItem(item);
    }

    public void update(Item newItem, EstimateItemDetail newEstimateItemDetail) {
        setItem(newItem);
        setAmount(newEstimateItemDetail.amount);
        setNote(newEstimateItemDetail.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
