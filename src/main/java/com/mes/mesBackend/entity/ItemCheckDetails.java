package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 품목별 검사항목 등록 세부리스트
 * 조건코드
 * 조건항목
 * 조건내용
 * 판정기준
 * 판정방법
 * 입력방법
 * 숫자입력포맷
 * 상한값
 * 하한값
 * 관리상한값
 * 관리하한값
 * 상한공차
 * 하한공차
 * 중심값
 * 검사주기
 * 표시순서
 * 최대차수
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "ITEM_CHECK_DETAILS")
@Data
public class ItemCheckDetails extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목별 검사항목 세부 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_CHECK_CATEGORY", columnDefinition = "bigint COMMENT '품목별 검사항목'")
    private ItemCheckCategory itemCheckCategory;    // 품목별 검사항목 등록 고유아이디

    @Column(name = "CONDITION_CODE", columnDefinition = "varchar(255) COMMENT '조건코드'")
    private String conditionCode;       // 조건코드

    @Column(name = "CONDITION_ITEM", columnDefinition = "varchar(255) COMMENT '조건항목'")
    private String conditionItem;       // 조건항목

    @Column(name = "CONDITION_CONTENT", columnDefinition = "varchar(255) COMMENT '조건내용'")
    private String conditionContent;    // 조건내용

    @Column(name = "CRITERIA_STANDARD", columnDefinition = "varchar(255) COMMENT '판정기준'")
    private String criteriaStandard;    // 판정기준

    @Column(name = "CRITERIA_METHOD", columnDefinition = "varchar(255) COMMENT '판정방법'")
    private String criteriaMethod;      // 판정방법

    @Column(name = "INPUT_METHOD", columnDefinition = "varchar(255) COMMENT '입력방법'")
    private String inputMethod;         // 입력방법

    @Column(name = "INPUT_FMT", columnDefinition = "varchar(255) COMMENT '숫자입력포맷'")
    private String inputFmt;            // 숫자입력포멧

    @Column(name = "USL", columnDefinition = "varchar(255) COMMENT '상한값'")
    private String usl;                 // 상한값

    @Column(name = "LSL", columnDefinition = "varchar(255) COMMENT '하한값'")
    private String lsl;                 // 하한값

    @Column(name = "M_USL", columnDefinition = "varchar(255) COMMENT '관리상한값'")
    private String masterUsl;           // 관리상한값

    @Column(name = "M_LSL", columnDefinition = "varchar(255) COMMENT '관리하한값'")
    private String masterLsl;           // 관리하한값

    @Column(name = "U_TOTAL_VAL", columnDefinition = "varchar(255) COMMENT '상한공차'")
    private String uTotalVal;           // 상한공차

    @Column(name = "L_TOTAL_VAL", columnDefinition = "varchar(255) COMMENT '하한공차'")
    private String lTotalVal;           // 하한공차

    @Column(name = "CL", columnDefinition = "varchar(255) COMMENT '중심값'")
    private String cl;                  // 중심값

    @Column(name = "CHECK_CYCLE", columnDefinition = "int COMMENT '검사주기'")
    private int checkCycle;             // 검사주기

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '표시순서'")
    private int orders;                // 표시순서

    @Column(name = "MAX_CNT", columnDefinition = "int COMMENT '최대차수'")
    private int maxCnt;                 // 최대차수

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
