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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REPAIR_CODE", columnDefinition = "bigint COMMENT '수리코드'", nullable = false)
    private RepairCode repairCode;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void add(EquipmentBreakdown equipmentBreakdown, RepairCode repairCode) {
        setEquipmentBreakdown(equipmentBreakdown);
        setRepairCode(repairCode);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void update(RepairCode newRepairCode) {
        setRepairCode(newRepairCode);
    }
}
