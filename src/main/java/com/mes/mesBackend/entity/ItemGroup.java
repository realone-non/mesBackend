package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목 그룹 등록
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_GROUPS")
@Data
public class ItemGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "GROUP_CODE", nullable = false)
    private String groupCode;            // 그룹코드

    @Column(name = "TOP_GROUP_CODE")
    private String topGroupCode;        // 상위그룹코드

    @Column(name = "TOP_GROUP_NAME")
    private String topGroupName;        // 상위그룹명

    @Column(name = "GROUP_NAME")
    private String groupName;           // 그룹명

    @Column(name = "DEFAULT_NAME")
    private String defaultName;         // 기본어명

    @Column(name = "ORDERS", nullable = false)
    private int orders;              // 순번

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;              // 사용
}
