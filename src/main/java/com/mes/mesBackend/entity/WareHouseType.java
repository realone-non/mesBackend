package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 창고유형
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WARE_HOUSE_TYPES")
@Data
public class WareHouseType extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;    // 창고유형 id

    @Column(name = "NAME", nullable = false, length = 10)
    private String name;    // 창고유형

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
