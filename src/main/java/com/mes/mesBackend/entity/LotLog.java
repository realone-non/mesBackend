package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

// lotMaster 로그
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "LOT_LOGS")
@Data
public class LotLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '로트 로그 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_ORDER_DETAIL", columnDefinition = "bigint COMMENT '작업지시'")
    private WorkOrderDetail workOrderDetail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT '로트마스터'")
    private LotMaster lotMaster;
}
