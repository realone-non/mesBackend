package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 창고 등록
* 검색 : 공장
* 창고코드(00001),
* 창고명(kitting창고,연구소창고,생산창고,영업창고),
* 창고유형(제품창고,공정창고,검사창고),
* 창고그룹,
* 작업장 / 일단 빼고 작업,
* 작업라인 / 일단 빼고 작업,
* 작업공정 / 일단 빼고 작업,
* 사용
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WARE_HOUSES")
@Data
public class WareHouse extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '창고 등록 고유아이디'")
    private Long id;

    @Column(name = "WARE_HOUSE_CODE", nullable = false, columnDefinition = "bigint COMMENT '창고코드'")
    private int wareHouseCode;  // 창고코드

    @Column(name = "NAME", columnDefinition = "bigint COMMENT '창고명'")
    private String name;   // 창고명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE_TYPE", columnDefinition = "bigint COMMENT '창고유형'")
    private WareHouseType wareHouseType;    // 창고유형

    @Column(name = "WARE_HOUSE_GROUP", columnDefinition = "bigint COMMENT '창고그룹'")
    private String wareHouseGroup;   // 창고그룹

//    @Column(name = "WORK_SHOP")
//    private String workShop;    // 작업장

//    @Column(name = "WORK_LINE")
//    private String workLine;    // 작업라인

//    @Column(name = "WORK_PROCESS")
//    private String workProcess; // 작업공정

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;      // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장 (검색)
}