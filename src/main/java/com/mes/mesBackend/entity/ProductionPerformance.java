package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
* 8-6. 생산실적 관리
* */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "PRODUCTION_PERFORMANCES")
@Data
public class ProductionPerformance extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '생산실적관리 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_ORDER_DETAIL", columnDefinition = "bigint COMMENT '작업지시'")
    private WorkOrderDetail workOrderDetail;

    // default value 0
    // 생산량
    @Column(name = "PRODUCTION_AMOUNT", columnDefinition = "int COMMENT '생산량'")
    private int productionAmount;

    // 자재입고
    @Column(name = "MATERIAL_INPUT", columnDefinition = "datetime(6) COMMENT '자재입고'")
    private LocalDateTime materialInput;

    // 원료혼합
    @Column(name = "MATERIAL_MIXING", columnDefinition = "datetime(6) COMMENT '원료혼합'")
    private LocalDateTime materialMixing;

    // 충진
    @Column(name = "FILLING", columnDefinition = "datetime(6) COMMENT '충진'")
    private LocalDateTime filling;

    // 캡조립
    @Column(name = "CAP_ASSEMBLY", columnDefinition = "datetime(6) COMMENT '캡조립'")
    private LocalDateTime capAssembly;

    // 라벨링
    @Column(name = "LABELING", columnDefinition = "datetime(6) COMMENT '라벨링'")
    private LocalDateTime labeling;

    // 포장
    @Column(name = "PACKAGING", columnDefinition = "datetime(6) COMMENT '포장'")
    private LocalDateTime packaging;

    // 출하
    @Column(name = "SHIPMENT", columnDefinition = "datetime(6) COMMENT '출하'")
    private LocalDateTime shipment;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'lot master'")
    private LotMaster lotMaster;
}
