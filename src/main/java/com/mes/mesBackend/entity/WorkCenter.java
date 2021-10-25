package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 작업장 등록
 * 검색조건 : 공장
 * 작업장코드(WC01),
 * 작업장명(1작업장,외주작업장(리버텍)),
 * 외주사(리버텍,신성테크),
 * Cost Center,
 * 사용,
 * 공장(안보여주고 검색에만 사용)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_CENTERS")
@Data
public class WorkCenter extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업장 등록 고유아이디'")
    private Long id;

    @Column(name = "WORK_CENTER_CODE", columnDefinition = "varchar(255) COMMENT '작업장 코드'")
    private String workCenterCode;      // 작업장코드

    @Column(name = "WORK_CENTER_NAME", columnDefinition = "varchar(255) COMMENT '작업장명'")
    private String workCenterName;      // 작업장명

    @Column(name = "OUT_COMPANY", columnDefinition = "varchar(255) COMMENT '외주사'")
    private String outCompany;          // 외주사

    @Column(name = "COST_CENTER", columnDefinition = "varchar(255) COMMENT 'Cost Center'")
    private String costCenter;          // Cost Center

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
