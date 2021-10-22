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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '대체품목등록 고유아이디'")
    private Long id;

    // 일대일 단방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;

    @Column(name = "SUB_ITEM_NO", columnDefinition = "varchar(255) COMMENT '대체품번'")
    private String subItemNO;   // 대체품번

    @Column(name = "SUB_ITEM_NAME", columnDefinition = "varchar(255) COMMENT '대체품명'")
    private String subItemName; // 대체품명

    @Column(name = "SUB_ORDERS", columnDefinition = "int COMMENT '대체순번'")
    private int subOrders;   // 대체순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;                      // 사용
}
