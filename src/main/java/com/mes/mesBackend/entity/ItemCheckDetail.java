package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 3-4-2.품목별 검사항목 등록 세부리스트
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
public class ItemCheckDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목별 검사항목 세부 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_CHECK_CATEGORY", columnDefinition = "bigint COMMENT '품목별 검사항목'", nullable = false)
    private ItemCheck itemCheckCategory;    // 품목별 검사항목 등록 고유아이디

    @Column(name = "CONDITION_CODE", columnDefinition = "varchar(255) COMMENT '조건코드'", nullable = false)
    private String conditionCode;       // 조건코드

    @Column(name = "CONDITION_ITEM", columnDefinition = "varchar(255) COMMENT '조건항목'", nullable = false)
    private String conditionItem;       // 조건항목

    @Column(name = "CONDITION_CONTENT", columnDefinition = "varchar(255) COMMENT '조건내용'", nullable = false)
    private String conditionContent;    // 조건내용

    @Column(name = "CRITERIA_STANDARD", columnDefinition = "varchar(255) COMMENT '판정기준'", nullable = false)
    private String criteriaStandard;    // 판정기준

    @Column(name = "CRITERIA_METHOD", columnDefinition = "varchar(255) COMMENT '판정방법'", nullable = false)
    private String criteriaMethod;      // 판정방법

    @Column(name = "INPUT_METHOD", columnDefinition = "varchar(255) COMMENT '입력방법'", nullable = false)
    private String inputMethod;         // 입력방법

    @Column(name = "INPUT_FMT", columnDefinition = "varchar(255) COMMENT '숫자입력포맷'", nullable = false)
    private String inputFmt;            // 숫자입력포멧

    @Column(name = "USL", columnDefinition = "float COMMENT '상한값'", nullable = false)
    private float usl;                 // 상한값

    @Column(name = "LSL", columnDefinition = "float COMMENT '하한값'", nullable = false)
    private float lsl;                 // 하한값

    @Column(name = "M_USL", columnDefinition = "float COMMENT '관리상한값'", nullable = false)
    private float masterUsl;           // 관리상한값

    @Column(name = "M_LSL", columnDefinition = "float COMMENT '관리하한값'", nullable = false)
    private float masterLsl;           // 관리하한값

    @Column(name = "U_TOTAL_VAL", columnDefinition = "float COMMENT '상한공차'", nullable = false)
    private float uTotalVal;           // 상한공차

    @Column(name = "L_TOTAL_VAL", columnDefinition = "float COMMENT '하한공차'", nullable = false)
    private float lTotalVal;           // 하한공차

    @Column(name = "CL", columnDefinition = "float COMMENT '중심값'", nullable = false)
    private float cl;                  // 중심값

    @Column(name = "CHECK_CYCLE", columnDefinition = "int COMMENT '검사주기'", nullable = false)
    private int checkCycle;             // 검사주기

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '표시순서'", nullable = false)
    private int orders;                // 표시순서

    @Column(name = "MAX_CNT", columnDefinition = "int COMMENT '최대차수'", nullable = false)
    private int maxCnt;                 // 최대차수

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void addJoin(ItemCheck itemCheck) {
        setItemCheckCategory(itemCheck);
    }

    public void update(ItemCheckDetail newItemCheckDetail) {
        setConditionCode(newItemCheckDetail.conditionCode);
        setConditionItem(newItemCheckDetail.conditionItem);
        setConditionContent(newItemCheckDetail.conditionContent);
        setCriteriaStandard(newItemCheckDetail.criteriaStandard);
        setCriteriaMethod(newItemCheckDetail.criteriaMethod);
        setInputMethod(newItemCheckDetail.inputMethod);
        setInputFmt(newItemCheckDetail.inputFmt);
        setUsl(newItemCheckDetail.usl);
        setLsl(newItemCheckDetail.lsl);
        setMasterUsl(newItemCheckDetail.masterUsl);
        setMasterLsl(newItemCheckDetail.masterLsl);
        setUTotalVal(newItemCheckDetail.uTotalVal);
        setLTotalVal(newItemCheckDetail.lTotalVal);
        setCl(newItemCheckDetail.cl);
        setCheckCycle(newItemCheckDetail.checkCycle);
        setOrders(newItemCheckDetail.orders);
        setMaxCnt(newItemCheckDetail.maxCnt);
        setUseYn(newItemCheckDetail.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
