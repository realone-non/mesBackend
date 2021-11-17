package com.mes.mesBackend.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/*
* 대체품목 등록
* 품번, 품명, 대체품번, 대체품명, 대체순번, 사용,
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SUB_ITEMS")
@Data
public class SubItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '대체품목등록 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SUB_ITEM", columnDefinition = "bigint COMMENT '대체 품목'", nullable = false)
    private Item subItem;

    @Column(name = "SUB_ORDERS", columnDefinition = "int COMMENT '대체순번'", nullable = false)
    private int subOrders;   // 대체순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;                      // 사용

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;           // 삭제여부

    public void addJoin(Item item, Item subItem) {
        setItem(item);
        setSubItem(subItem);
    }

    public void update(SubItem newEntity, Item newItem, Item newSubItem) {
        setItem(newItem);
        setSubItem(newSubItem);
        setSubOrders(newEntity.subOrders);
        setUseYn(newEntity.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
