package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DETAIL_NAVS")
@Data
public class DetailNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '디테일 네비게이션 고유아이디'")
    private Long id;

    @Column(name = "NAME", columnDefinition = "bigint COMMENT '네비게이션 명'")
    private String name;

    @Column(name = "LEVEL", columnDefinition = "bigint COMMENT '유저레벨'")
    private int level;  // 유저 레벨

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;  // 사용여부

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SUB_NAVS_ID", columnDefinition = "bigint COMMENT '서브 네비게이션'")
    private SubNavigation subNavigation;

    @Column(name = "ORDERS", columnDefinition = "bigint COMMENT '출력순번'")
    private int orders;     // 출력순번

    public void put(DetailNavigation detailNavigation) {
        setName(detailNavigation.name);
        setLevel(detailNavigation.level);
        setOrders(detailNavigation.orders);
    }
}
