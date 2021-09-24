package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 단위
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "UNITS")
@Data
public class Unit extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "UNIT_CODE", nullable = false, length = 10)
    private String unitCode;    // 단위코드

    @Column(name = "UNIT_CODE_NAME", nullable = false, length = 15)
    private String unitCodeName;    // 단위명

    @Column(name = "SYMBOL")
    private String symbol;      // 심볼

    @Column(name = "DEFAULT_UNIT")
    private String defaultUnit; // 기본단위

    @Column(name = "BASE_SCALE", nullable = false)
    private int baseScale;      // base대비 율

    @Column(name = "DECIMAL_POINT", nullable = false)
    private int decimalPoint;   // 소수점자리수

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;      // 사용여부
}
