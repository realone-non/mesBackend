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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '계측기 등록 고유아이디'")
    private Long id;

    @Column(name = "GAUGE_CODE", nullable = false, unique = true, columnDefinition = "bigint COMMENT 'GAUGE코드'")
    private String gaugeCode;       // GAUGE코드

    @Column(name = "GAUGE_NAME", nullable = false, columnDefinition = "bigint COMMENT 'GAUGE명'")
    private String gaugeName;       // GAUGE명

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAUGE_TYPE", nullable = false, columnDefinition = "bigint COMMENT 'GAUGE유형'")
    private GaugeType gaugeType;       // GAUGE유형

    @Column(name = "MODEL", nullable = false, columnDefinition = "bigint COMMENT '규격,모델'")
    private String model;           // 규격&모델

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT", nullable = false, columnDefinition = "bigint COMMENT '부서'")
    private Department department;      // 사용부서

    @Column(name = "PURCHASE_DATE", columnDefinition = "bigint COMMENT '구매일자'")
    private LocalDate purchaseDate;     // 구매일자

    @Column(name = "MAKER", columnDefinition = "bigint COMMENT '생산업체명'")
    private String maker;               // 생산업체명

    @Column(name = "CALIBRATION_CYCLE", columnDefinition = "bigint COMMENT '검교정주기'")
    private int calibrationCycle;      // 검교정주기

    @Column(name = "CALIBRATION_LAST_DATE", columnDefinition = "bigint COMMENT '최종 검교정일자'")
    private LocalDate calibrationLastDate;      // 최종 검교정일자

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
