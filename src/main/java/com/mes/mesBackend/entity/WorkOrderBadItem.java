package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

// 8-5. 불량유형정보
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_ORDER_BAD_ITEMS")
@Data
public class WorkOrderBadItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 불량 등록 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BAD_ITEM", columnDefinition = "bigint COMMENT '불량항목'", nullable = false)
    private BadItem badItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_LOG", columnDefinition = "bigint COMMENT '로트 로그'", nullable = false)
    private LotLog lotLog;

    @Column(name = "BAD_ITEM_AMOUNT", columnDefinition = "int COMMENT '불량수량'")
    private int badItemAmount;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;

    public void update(int badItemAmount) {
        setBadItemAmount(badItemAmount);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void add(LotLog lotLog, BadItem badItem, int badItemAmount) {
        setLotLog(lotLog);
        setBadItem(badItem);
        setBadItemAmount(badItemAmount);
    }
}
