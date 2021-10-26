package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MEASURE_CALIBRATIONS")
@Data
public class MeasureCalibration extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '계측기 검교정 실적 고유아이디'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEASURE", columnDefinition = "bigint COMMENT '계측기'")
    private Measure measure;        // 계측기 정보

    @Column(name = "CALIBRATION_METHOD", columnDefinition = "varchar(255) COMMENT '검교정 방법'")
    private String calibrationMethod;       // 검교정 방법

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'")
    private Client client;          // 검교정의뢰처

    @Column(name = "REQUEST_DATE", columnDefinition = "date COMMENT '요청일자'")
    private LocalDate requestDate;                  // 요청일자

    @Column(name = "CALIBRATION_DATE", columnDefinition = "varchar(255) COMMENT '검교정일자'")
    private LocalDate calibrationDate;              // 검교정일자

    @Column(name = "RESULT", columnDefinition = "varchar(255) COMMENT '검교정 결과'")
    private String result;      // 검교정 결과

    @Column(name = "PRICE", columnDefinition = "int COMMENT '검교정비용'")
    private int price;       // 검교정 비용

    @Column(name = "REPORT", columnDefinition = "varchar(255) COMMENT '성적서'")
    private String report;      // 성적서

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT", columnDefinition = "bigint COMMENT '사용부서'")
    private Department department;      // 사용부서
}
