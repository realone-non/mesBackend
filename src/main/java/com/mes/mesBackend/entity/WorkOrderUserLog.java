package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 작업지시 작업자 정보 변경 기록
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "WORK_ORDER_USER_LOGS")
@Data
public class WorkOrderUserLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 작업자 변경 기록 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_ORDER_DETAIL", columnDefinition = "bigint COMMENT '작업지시'", nullable = false)
    private WorkOrderDetail workOrderDetail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '작업자'", nullable = false)
    private User user;

    @Column(name = "PRODUCT_AMOUNT", columnDefinition = "int COMMENT '작업수량'")
    private int productAmount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT_LOT_MASTER", columnDefinition = "bigint COMMENT '설비 로트'")
    private LotMaster equipmentLotMaster;

    public void create(WorkOrderDetail workOrderDetail, User user, int productAmount, LotMaster equipmentLotMaster) {
        setWorkOrderDetail(workOrderDetail);
        setUser(user);
        setProductAmount(productAmount);
        setEquipmentLotMaster(equipmentLotMaster);
    }
}
