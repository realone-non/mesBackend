package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 창고
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WARE_HOUSES")
@Data
public class WareHouse extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "WARE_HOUSE_CODE", nullable = false)
    private int wareHouseCode;  // 창고코드

    @Column(name = "NAME")
    private String name;   // 창고명

    @ManyToOne @JoinColumn(name = "WARE_HOUSE_TYPES_ID")
    private WareHouseType wareHouseType;    // 창고유형

    @Column(name = "WARE_HOUSE_GROUP")
    private String wareHouseGroup;   // 창고그룹

    @Column(name = "WORK_SHOP")
    private String workShop;    // 작업장

    @Column(name = "WORK_LINE")
    private String workLine;    // 작업라인

    @Column(name = "WORK_PROCESS")
    private String workProcess; // 작업공정

    @Column(name = "USE_YN")
    private boolean useYn;      // 사용여부
}