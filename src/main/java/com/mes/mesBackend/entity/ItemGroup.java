package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목 그룹 등록
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "ITEM_GROUPS")
@Data
public class ItemGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목그룹 등록 고유아이디'")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)    TODO: 삭제
//    @JoinColumn(name = "ITEM_GROUP_CODES_ID", columnDefinition = "bigint COMMENT '그룹코드'", nullable = false)
//    private ItemGroupCode itemGroupCode;

    @Column(name = "TOP_GROUP_CODE", columnDefinition = "varchar(255) COMMENT '상위그룹코드'")
    private String topGroupCode;        // 상위그룹코드

    @Column(name = "TOP_GROUP_NAME", columnDefinition = "varchar(255) COMMENT '상위그룹명'")
    private String topGroupName;        // 상위그룹명

    @Column(name = "GROUP_NAME", columnDefinition = "varchar(255) COMMENT '그룹명'")
    private String groupName;           // 그룹명

    @Column(name = "DEFAULT_NAME", columnDefinition = "varchar(255) COMMENT '기본어멷'")
    private String defaultName;         // 기본어명

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '순번'")
    private int orders;              // 순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;              // 사용

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;       // 삭제여부

    public void put(ItemGroup newItemGroup) {
        setTopGroupCode(newItemGroup.topGroupCode);
        setTopGroupName(newItemGroup.topGroupName);
        setGroupName(newItemGroup.groupName);
        setDefaultName(newItemGroup.defaultName);
        setOrders(newItemGroup.orders);
        setUseYn(newItemGroup.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
