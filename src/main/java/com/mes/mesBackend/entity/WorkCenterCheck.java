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
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "WORK_CENTER", nullable = false)
    private WorkCenter workCenter;      // 작업장코드

    @OneToOne @JoinColumn(name = "CHECK_TYPE" ,nullable = false)
    private CheckType checkType;        // 점검유형

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
