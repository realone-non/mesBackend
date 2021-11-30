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
 * 3-5-3. 계측기 등록
 * 검색: 공장, GAUGE유형, 검교정대상, R&R대상, 사용율(이상)
 * GAUCE코드 (EM-1001)
 * GAUCE명
 * GAUCE유형
 * 규격&모델
 * 사용부서
 * 구매일자
 * 생산업체명
 * 검교정주기
 * 최종 검교정일자
 * 차기 검교정일자 (최종 검교정일자 + 일년)
 * 사용
 **/
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "MEASURES")
@Data
public class Measure extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '계측기 등록 고유아이디'")
    private Long id;

    @Column(name = "GAUGE_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT 'GAUGE코드'")
    private String gaugeCode;       // GAUGE코드

    @Column(name = "GAUGE_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT 'GAUGE명'")
    private String gaugeName;       // GAUGE명

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "GAUGE_TYPE", nullable = false, columnDefinition = "bigint COMMENT 'GAUGE유형'")
    private GaugeType gaugeType;       // GAUGE유형

    @Column(name = "MODEL", nullable = false, columnDefinition = "varchar(255) COMMENT '규격,모델'")
    private String model;           // 규격&모델

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DEPARTMENT", nullable = false, columnDefinition = "bigint COMMENT '부서'")
    private Department department;      // 사용부서

    @Column(name = "PURCHASE_DATE", columnDefinition = "datetime COMMENT '구매일자'", nullable = false)
    private LocalDateTime purchaseDate;     // 구매일자

    @Column(name = "MAKER", columnDefinition = "varchar(255) COMMENT '생산업체명'", nullable = false)
    private String maker;               // 생산업체명

    @Column(name = "CALIBRATION_CYCLE", columnDefinition = "bigint COMMENT '검교정주기'", nullable = false)
    private Long calibrationCycle;      // 검교정주기

    @Column(name = "CALIBRATION_LAST_DATE", columnDefinition = "datetime COMMENT '최종 검교정일자'", nullable = false)
    private LocalDateTime calibrationLastDate;      // 최종 검교정일자

    @Column(name = "CALIBRATION_NEXT_DATE", columnDefinition = "datetime COMMENT '차기 검교정일자'", nullable = false)
    private LocalDateTime calibrationNextDate;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void addMapping(GaugeType gaugeType, Department department, LocalDateTime calibrationLastDate, Long calibrationCycle) {
        setGaugeType(gaugeType);
        setDepartment(department);
        setCalibrationNextDate(calibrationLastDate.plusMonths(calibrationCycle));
    }

    public void update(Measure newMeasure, GaugeType newGaugeType, Department newDepartment) {
        setGaugeCode(newMeasure.gaugeCode);
        setGaugeName(newMeasure.gaugeName);
        setGaugeType(newGaugeType);
        setModel(newMeasure.model);
        setDepartment(newDepartment);
        setPurchaseDate(newMeasure.purchaseDate);
        setMaker(newMeasure.maker);
        setCalibrationCycle(newMeasure.calibrationCycle);
        setCalibrationLastDate(newMeasure.calibrationLastDate);
        setUseYn(newMeasure.useYn);
        setCalibrationNextDate(newMeasure.calibrationLastDate.plusMonths(newMeasure.calibrationCycle));
    }

    public void delete() {
        setDeleteYn(true);
    }
}
