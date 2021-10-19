package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 계측기 등록
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MEASURES")
@Data
public class Measure extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "GAUGE_CODE", nullable = false, unique = true)
    private String gaugeCode;       // GAUGE코드

    @Column(name = "GAUGE_NAME", nullable = false)
    private String gaugeName;       // GAUGE명

    @OneToOne @JoinColumn(name = "GAUGE_TYPE_ID", nullable = false)
    private GaugeType gaugeType;       // GAUGE유형

    @Column(name = "MODEL", nullable = false)
    private String model;           // 규격&모델

    @OneToOne @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    private Department department;      // 사용부서

    @Column(name = "PURCHASE_DATE")
    private LocalDate purchaseDate;     // 구매일자

    @Column(name = "MAKER")
    private String maker;               // 생산업체명

    @Column(name = "CALIBRATION_CYCLE")
    private int calibrationCycle;      // 검교정주기

    @Column(name = "CALIBRATION_LAST_DATE")
    private LocalDate calibrationLastDate;      // 최종 검교정일자

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
