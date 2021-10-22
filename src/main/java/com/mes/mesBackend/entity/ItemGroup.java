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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목그룹 등록 고유아이디'")
    private Long id;

    @Column(name = "GROUP_CODE", nullable = false, columnDefinition = "bigint COMMENT '그룹코드'")
    private String groupCode;            // 그룹코드

    @Column(name = "TOP_GROUP_CODE", columnDefinition = "bigint COMMENT '상위그룹코드'")
    private String topGroupCode;        // 상위그룹코드

    @Column(name = "TOP_GROUP_NAME", columnDefinition = "bigint COMMENT '상위그룹명'")
    private String topGroupName;        // 상위그룹명

    @Column(name = "GROUP_NAME", columnDefinition = "bigint COMMENT '그룹명'")
    private String groupName;           // 그룹명

    @Column(name = "DEFAULT_NAME", columnDefinition = "bigint COMMENT '기본어멷'")
    private String defaultName;         // 기본어명

    @Column(name = "ORDERS", nullable = false, columnDefinition = "bigint COMMENT '순번'")
    private int orders;              // 순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;              // 사용
}
