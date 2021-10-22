package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 작업장별 점검항목 등록
 * 검색: 공장, 작업장, 점검유형
 * 작업장코드(1작업장(본사))
 * 점검유형(일,월,분기)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_CENTER_CHECKS")
@Data
public class WorkCenterCheck extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업장별 점검항목 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_CENTER", nullable = false, columnDefinition = "bigint COMMENT '작업장'")
    private WorkCenter workCenter;      // 작업장코드

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHECK_TYPE" ,nullable = false, columnDefinition = "bigint COMMENT '점검유형'")
    private CheckType checkType;        // 점검유형

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'",nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
