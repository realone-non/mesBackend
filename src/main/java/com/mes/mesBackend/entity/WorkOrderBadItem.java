package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 8-5. 불량유형정보
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "WORK_ORDER_BAD_ITEMS")
@Data
public class WorkOrderBadItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 불량 등록 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BAD_ITEM", columnDefinition = "bigint COMMENT '불량항목'", nullable = false)
    private BadItem badItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_ORDER_DETAIL", columnDefinition = "bigint COMMENT '작업지시'", nullable = false)
    private WorkOrderDetail workOrderDetail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT '설비 lot master'", nullable = false)
    private LotMaster lotMaster;

    @Column(name = "BAD_ITEM_AMOUNT", columnDefinition = "int COMMENT '불량수량'")
    private int badItemAmount;

    @Enumerated(STRING)
    @Column(name = "DIVISION", columnDefinition = "varchar(255) COMMENT '불량 생성 구분'")
    private LotMasterDivision division;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;

    public void update(int badItemAmount) {
        setBadItemAmount(badItemAmount);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void create(
            BadItem badItem,
            WorkOrderDetail workOrderDetail,
            LotMaster lotMaster,
            int badItemAmount,
            LotMasterDivision lotMasterDivision
    ) {
        setBadItem(badItem);
        setWorkOrderDetail(workOrderDetail);
        setLotMaster(lotMaster);
        setBadItemAmount(badItemAmount);
        setDivision(lotMasterDivision);
    }

    public void popCreate(
            BadItem badItem,
            WorkOrderDetail workOrderDetail,
            LotMaster lotMaster,
            int badItemAmount,
            LotMasterDivision division
    ) {
        setBadItem(badItem);
        setWorkOrderDetail(workOrderDetail);
        setLotMaster(lotMaster);
        setBadItemAmount(badItemAmount);
        setDivision(division);
    }
}
