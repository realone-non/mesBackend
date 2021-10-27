package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 3-1-7. 단위등록
* 단위코드
* 단위명
* 심볼
* 기본단위
* Base 대비 율
* 소수점 자리 수
* 사용
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "UNITS")
@Data
public class Unit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '단위 등록 고유아이디'")
    private Long id;

    @Column(name = "UNIT_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '단위코드'")
    private String unitCode;    // 단위코드

    @Column(name = "UNIT_CODE_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '단위명'")
    private String unitCodeName;    // 단위명

    @Column(name = "SYMBOL", columnDefinition = "varchar(255) COMMENT '심볼'")
    private String symbol;      // 심볼

    @Column(name = "DEFAULT_UNIT", columnDefinition = "varchar(255) COMMENT '기본단위'")
    private String defaultUnit; // 기본단위

    @Column(name = "BASE_SCALE", nullable = false, columnDefinition = "float COMMENT 'base 대비 율'")
    private float baseScale;      // base대비 율

    @Column(name = "DECIMAL_POINT", nullable = false, columnDefinition = "int COMMENT '소수점자리수'")
    private int decimalPoint;   // 소수점자리수

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    // 수정 메서드
    public void putUnit(Unit newUnit) {
        setUnitCode(newUnit.unitCode);
        setUnitCodeName(newUnit.unitCodeName);
        setSymbol(newUnit.symbol);
        setDefaultUnit(newUnit.defaultUnit);
        setBaseScale(newUnit.baseScale);
        setDecimalPoint(newUnit.decimalPoint);
        setUseYn(newUnit.useYn);
    }
}
