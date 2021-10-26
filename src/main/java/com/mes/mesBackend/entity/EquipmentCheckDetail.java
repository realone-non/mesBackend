package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "EQUIPMENT_CHECK_DETAILS")
@Data
public class EquipmentCheckDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비점검 세부항목 고유아이디'")
    private Long id;

    @Column(name = "CHECK_CATEGORY", columnDefinition = "varchar(255) COMMENT '점검항목'")
    private String checkCategory;       // 점검항목

    @Column(name = "CHECK_CONTENT", columnDefinition = "varchar(255) COMMENT '점검내용'")
    private String checkContent;        // 점검내용

    @Column(name = "CRITERIA_STANDARD", columnDefinition = "varchar(255) COMMENT '판정기준'")
    private String criteriaStandard;        // 판정기준

    @Column(name = "CRITERIA_METHOD", columnDefinition = "varchar(255) COMMENT '판정방법'")
    private String criteriaMethod;      // 판정방법

    @Column(name = "USL", columnDefinition = "varchar(255) COMMENT '상한값'")
    private String usl;     // 상한값

    @Column(name = "LSL", columnDefinition = "varchar(255) COMMENT '하한값'")
    private String lsl;     // 하한값

    @Column(name = "RESULT", columnDefinition = "varchar(255) COMMENT '결과값'")
    private String result;      // 결과값

    @Column(name = "REGISTER_TYPE", columnDefinition = "varchar(255) COMMENT '등록유형'")
    private String registerType;        // 등록유형

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPMENT_CHECK", columnDefinition = "bigint COMMENT '설비 점검'", nullable = false)
    private EquipmentCheck equipmentCheck;
}
