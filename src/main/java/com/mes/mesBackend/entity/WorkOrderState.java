package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.OrderState;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 8-1. 작업지시 상태 이력 정보
//@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "WORK_ORDER_STATES")
@Data
public class WorkOrderState extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 상태 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_ORDER_DETAIL", columnDefinition = "bigint COMMENT '작업 지시'")
    private WorkOrderDetail workOrderDetail;

    @Column(name = "WORK_ORDER_DATE_TIME", columnDefinition = "datetime COMMENT '작업일시'")
    private LocalDateTime workOrderDateTime;

    @Enumerated(STRING)
    @Column(name = "ORDER_STATE", columnDefinition = "varchar(255) COMMENT '지시상태'")
    private OrderState orderState;

    public WorkOrderState(WorkOrderDetail workOrderDetail, LocalDateTime workOrderDateTime, OrderState orderState) {
        this.workOrderDetail = workOrderDetail;
        this.workOrderDateTime = workOrderDateTime;
        this.orderState = orderState;
    }
}
