package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 3-5-2. 설비 보전항목 등록
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "EQUIPMENT_MAINTENANCES")
@Data
public class EquipmentMaintenance extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '고유아이디'")
    private Long id;

    @Column(name = "MAINTENANCE_CODE", columnDefinition = "varchar(255) COMMENT '보전항목코드'", nullable = false)
    private String maintenanceCode;

    @Column(name = "MAINTENANCE_NAME", columnDefinition = "varchar(255) COMMENT '보전항목명'", nullable = false)
    private String maintenanceName;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "PARENT", columnDefinition = "varchar(255) COMMENT '상위항목'")
    private String parent;

    @Column(name = "PARENT_NAME", columnDefinition = "varchar(255) COMMENT '상위항목명'")
    private String parentName;

    public void update(EquipmentMaintenance newEquipmentMaintenance) {
        setMaintenanceCode(newEquipmentMaintenance.maintenanceCode);
        setMaintenanceName(newEquipmentMaintenance.maintenanceName);
        setUseYn(newEquipmentMaintenance.useYn);
        setParent(newEquipmentMaintenance.parent);
        setParentName(newEquipmentMaintenance.parentName);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
