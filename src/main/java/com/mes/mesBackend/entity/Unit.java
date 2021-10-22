package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 단위 등록
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "UNITS")
@Data
public class Unit extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '단위 등록 고유아이디'")
    private Long id;

    @Column(name = "UNIT_CODE", nullable = false, columnDefinition = "bigint COMMENT '단위코드'")
    private String unitCode;    // 단위코드

    @Column(name = "UNIT_CODE_NAME", nullable = false, columnDefinition = "bigint COMMENT '단위명'")
    private String unitCodeName;    // 단위명

    @Column(name = "SYMBOL", columnDefinition = "bigint COMMENT '심볼'")
    private String symbol;      // 심볼

    @Column(name = "DEFAULT_UNIT", columnDefinition = "bigint COMMENT '기본단위'")
    private String defaultUnit; // 기본단위

    @Column(name = "BASE_SCALE", nullable = false, columnDefinition = "bigint COMMENT 'base대비 율'")
    private int baseScale;      // base대비 율

    @Column(name = "DECIMAL_POINT", nullable = false, columnDefinition = "bigint COMMENT '소수점자리수'")
    private int decimalPoint;   // 소수점자리수

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;      // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
