package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 작업장별 점검항목 등록 -> 작업장별 세부 점검항목 등록 리스트
 * 점검코드 (001,002)
 * 점검항목 (설비상태,002sss,0004,0051)
 * 점검내용 (소음)
 * 판정기준 (10ppm)
 * 판정방법 (버니어캘리퍼스,육안)
 * 입력방법 (숫자, ok/ng)
 * 숫자입력포맷 (N2)
 * 상한값 (232,000)
 * 하한값 (23232,000)
 * 표시순서 (1,2,3,4)
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_CENTER_CHECK_TYPE_LISTS")
@Data
public class WorkCenterCheckTypeList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업장별 세부 점검항목 고유아이디'")
    private Long id;

    // 다대일 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "WORK_CENTER_CHECK", nullable = false, columnDefinition = "bigint COMMENT '작업장별 점검항목'")
    private WorkCenterCheck workCenterCheck;    // 작업장별 점검항목

    @Column(name = "CHECK_CODE", nullable = false, unique = true, columnDefinition = "int COMMENT '점검코드'")
    private int checkCode;      // 점검코드

    @Column(name = "CHECK_CATEGORY", nullable = false, columnDefinition = "varchar(255) COMMENT '점검항목'")
    private String checkCategory;   // 점검항목

    @Column(name = "CHECK_CONTENT", nullable = false, columnDefinition = "varchar(255) COMMENT '점검내용'")
    private String checkContent;    // 점검내용

    @Column(name = "CHECK_CRITERIA", nullable = false, columnDefinition = "varchar(255) COMMENT '판정기준'")
    private String checkCriteria;        // 판정기준

    @Column(name = "CHECK_METHOD", nullable = false, columnDefinition = "varchar(255) COMMENT '판정방법'")
    private String checkMethod;        // 판정방법

    @Column(name = "INPUT_TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT '입력방법'")
    private String inputType;       // 입력방법

    @Column(name = "INPUT_NUMBER_FORMAT", columnDefinition = "varchar(255) COMMENT '숫자입력포맷'")
    private String inputNumberFormat;   // 숫자입력포맷

    @Column(name = "USL", columnDefinition = "int COMMENT '상한값'")
    private int usl;        // 상한값

    @Column(name = "LSL", columnDefinition = "int COMMENT '하한값'")
    private int lsl;        // 하한값

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '표시순서'")
    private int orders;     // 표시순서

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
