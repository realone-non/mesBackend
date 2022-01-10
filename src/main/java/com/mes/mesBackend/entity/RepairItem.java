package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 17-2. 설비 고장 수리 내역 수리항목
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "REPAIR_ITEMS")
@Data
public class RepairItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수리항목 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT_BREAKDOWN", columnDefinition = "bigint COMMENT '설비고장수리내역'", nullable = false)
    private EquipmentBreakdown equipmentBreakdown;

    @Column(name = "REPAIR_CODE", columnDefinition = "varchar(255) COMMENT '수리코드'", nullable = false)
    private String repairCode;

    @Column(name = "REPAIR_CONTENT", columnDefinition = "varchar(255) COMMENT '수리내용'", nullable = false)
    private String repairContent;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;
}
