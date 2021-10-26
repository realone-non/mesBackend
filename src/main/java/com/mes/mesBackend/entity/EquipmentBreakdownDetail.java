package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 17-2. 수리항목,부품,작업자 정보
 * 수리코드,수리내용
 * 수리부품,수리부품명,수량,비고
 * 보전원 ID
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "EQUIPMENT_BREAKDOWN_DETAILS")
@Data
public class EquipmentBreakdownDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비고장 수리 항목 고유아이디'")
    private Long id;

    @Column(name = "REPAIR_CODE", columnDefinition = "varchar(255) COMMENT '수리코드'")
    private String repairCode;      // 수리코드

    @Column(name = "REPAIR_CONTENT", columnDefinition = "varchar(255) COMMENT '수리내용'")
    private String repairContent;       // 수리내용

    @Column(name = "REPAIR_COMPONENT", columnDefinition = "varchar(255) COMMENT '수리부품'")
    private String repairComponent;     // 수리부품

    @Column(name = "REPAIR_COMPONENT_NAME", columnDefinition = "varchar(255) COMMENT '수리부품명'")
    private String repairComponentName;     // 수리부품명

    @Column(name = "AMOUNT", columnDefinition = "varchar(255) COMMENT '수량'")
    private String amount;              // 수량

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                // 비고

    @Column(name = "REPAIRER_ID", columnDefinition = "varchar(255) COMMENT '보전원 ID'")
    private String repairerID;                // 보전원Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPMENT_BREAKDOWN", columnDefinition = "bigint COMMENT '설비 고장수리정보'")
    private EquipmentBreakdown equipmentBreakdown;      // 설비 고장 수리 내역
}
