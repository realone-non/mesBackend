package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.CalibrationMethod;
import com.mes.mesBackend.entity.enumeration.CalibrationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 17-5. 계측기 검교정 실적 등록
 * 검색: 공장,검정처,측정기유형,검정기간
 * 계측기코드     Measure
 * 계측기명      Measure
 * 계측기유형     Measure
 * 검교정방법
 * 검교정의뢰처        Client
 * 요청일자
 * 검교정일자
 * 검교정 결과
 * 검교정비용
 * 성적서
 * 비고
 * 사용부서
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "MEASURE_CALIBRATIONS")
@Data
public class MeasureCalibration extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '계측기 검교정 실적 고유아이디'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEASURE", columnDefinition = "bigint COMMENT '계측기'", nullable = false)
    private Measure measure;        // 계측기 정보

    @Enumerated(STRING)
    @Column(name = "CALIBRATION_METHOD", columnDefinition = "varchar(255) COMMENT '검교정 방법'", nullable = false)
    private CalibrationMethod calibrationMethod;       // 검교정 방법

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DEPARTMENT", columnDefinition = "bigint COMMENT '부서'", nullable = false)
    private Department requestDepartment;          // 검교정의뢰처

    @Column(name = "REQUEST_DATE", columnDefinition = "date COMMENT '요청일자'", nullable = false)
    private LocalDate requestDate;                  // 요청일자

    @Column(name = "CALIBRATION_DATE", columnDefinition = "varchar(255) COMMENT '검교정일자'", nullable = false)
    private LocalDate calibrationDate;              // 검교정일자

    @Enumerated(STRING)
    @Column(name = "CALIBRATION_RESULT", columnDefinition = "varchar(255) COMMENT '검교정 결과'", nullable = false)
    private CalibrationResult calibrationResult;      // 검교정 결과

    @Column(name = "PRICE", columnDefinition = "int COMMENT '검교정비용'", nullable = false)
    private int price;       // 검교정 비용

    @Column(name = "REPORT", columnDefinition = "varchar(255) COMMENT '성적서'")
    private String reportFileUrl;      // 성적서

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void add(Measure measure, Department department) {
        setMeasure(measure);
        setRequestDepartment(department);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void update(MeasureCalibration newMeasureCalibration, Measure newMeasure, Department newDepartment) {
        setMeasure(newMeasure);
        setCalibrationMethod(newMeasureCalibration.calibrationMethod);
        setRequestDepartment(newDepartment);
        setRequestDate(newMeasureCalibration.requestDate);
        setCalibrationDate(newMeasureCalibration.calibrationDate);
        setCalibrationResult(newMeasureCalibration.calibrationResult);
        setPrice(newMeasureCalibration.price);
        setNote(newMeasureCalibration.note);
    }
}
