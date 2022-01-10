package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

/*
 * 17-1. 설비 점검 세부 항목
 * 점검항목
 * 점검내용
 * 판정기준
 * 판정방법
 * 상한값
 * 하한값
 * 결과값
 * 등록유형
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "EQUIPMENT_CHECK_DETAILS")
@Data
public class EquipmentCheckDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비점검 세부항목 고유아이디'")
    private Long id;

    @Column(name = "CHECK_CONTENT", columnDefinition = "varchar(255) COMMENT '점검내용'", nullable = false)
    private String checkContent;        // 점검내용

    @Column(name = "CRITERIA_STANDARD", columnDefinition = "varchar(255) COMMENT '판정기준'", nullable = false)
    private String criteriaStandard;        // 판정기준

    @Column(name = "CRITERIA_METHOD", columnDefinition = "varchar(255) COMMENT '판정방법'", nullable = false)
    private String criteriaMethod;      // 판정방법

    @Column(name = "USL", columnDefinition = "varchar(255) COMMENT '상한값'", nullable = false)
    private float usl;     // 상한값

    @Column(name = "LSL", columnDefinition = "varchar(255) COMMENT '하한값'", nullable = false)
    private float lsl;     // 하한값

    @Column(name = "RESULT", columnDefinition = "varchar(255) COMMENT '결과값'", nullable = false)
    private String result;      // 결과값

    @Column(name = "REGISTER_TYPE", columnDefinition = "varchar(255) COMMENT '등록유형'", nullable = false)
    private String registerType;        // 등록유형

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT", columnDefinition = "bigint COMMENT '설비정보'", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT_MAINTENANCE", columnDefinition = "bigint COMMENT '설비보전항목'")
    private EquipmentMaintenance equipmentMaintenance;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void add(Equipment equipment, EquipmentMaintenance equipmentMaintenance) {
        setEquipment(equipment);
        setEquipmentMaintenance(equipmentMaintenance);
    }

    public void update(EquipmentCheckDetail newEquipmentCheckDetail, EquipmentMaintenance newEquipmentMaintenance) {
        setEquipmentMaintenance(newEquipmentMaintenance);
        setCheckContent(newEquipmentCheckDetail.checkContent);
        setCriteriaStandard(newEquipmentCheckDetail.criteriaStandard);
        setCriteriaMethod(newEquipmentCheckDetail.criteriaMethod);
        setUsl(newEquipmentCheckDetail.usl);
        setLsl(newEquipmentCheckDetail.lsl);
        setResult(newEquipmentCheckDetail.result);
        setRegisterType(newEquipmentCheckDetail.registerType);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
