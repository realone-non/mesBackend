package com.mes.mesBackend.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    // 일대일 단방향
    @OneToOne @JoinColumn(name = "ITEMS_ID")
    private Item item;

    @Column(name = "SUB_ITEM_NO")
    private String subItemNO;   // 대체품번

    @Column(name = "SUB_ITEM_NAME")
    private String subItemName; // 대체품명

    @Column(name = "SUB_ORDERS")
    private int subOrders;   // 대체순번

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;                      // 사용
}
