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
@Entity(name = "ITEM_CHECK_DETAIL_LISTS")
@Data
public class ItemCheckDetailList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @ManyToOne @JoinColumn(name = "ITEM_CHECK_CATEGORY_ID")
    private ItemCheckCategory itemCheckCategory;    // 품목별 검사항목 등록 고유아이디

    @Column(name = "CONDITION_CODE")
    private String conditionCode;       // 조건코드

    @Column(name = "CONDITION_ITEM")
    private String conditionItem;       // 조건항목

    @Column(name = "CONDITION_CONTENT")
    private String conditionContent;    // 조건내용

    @Column(name = "CRITERIA_STANDARD")
    private String criteriaStandard;    // 판정기준

    @Column(name = "CRITERIA_METHOD")
    private String criteriaMethod;      // 판정방법

    @Column(name = "INPUT_METHOD")
    private String inputMethod;         // 입력방법

    @Column(name = "INPUT_FMT")
    private String inputFmt;            // 숫자입력포멧

    @Column(name = "USL")
    private String usl;                 // 상한값

    @Column(name = "LSL")
    private String lsl;                 // 하한값

    @Column(name = "M_USL")
    private String masterUsl;           // 관리상한값

    @Column(name = "M_LSL")
    private String masterLsl;           // 관리하한값

    @Column(name = "U_TOTAL_VAL")
    private String uTotalVal;           // 상한공차

    @Column(name = "L_TOTAL_VAL")
    private String lTotalVal;           // 하한공차

    @Column(name = "CL")
    private String cl;                  // 중심값

    @Column(name = "CHECK_CYCLE")
    private int checkCycle;             // 검사주기

    @Column(name = "ORDERS")
    private int orders;                // 표시순서

    @Column(name = "MAX_CNT")
    private int maxCnt;                 // 최대차수

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
