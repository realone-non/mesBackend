package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SUB_NAVS")
@Data
public class SubNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '서브 네비게이션'")
    private Long id;

    @Column(name = "NAME", columnDefinition = "varchar(255) COMMENT '네이게이션 명'")
    private String name;

    @Column(name = "LEVEL", columnDefinition = "int COMMENT '유저레벨'")
    private int level;  // 유저 레벨

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;  // 사용여부

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MAIN_NAVS_ID", columnDefinition = "bigint COMMENT '메인 네비게이션'")
    private MainNavigation mainNavigation;

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '출력순번'")
    private int orders;     // 출력순번

    public void put(SubNavigation newSubNavigation) {
        setName(newSubNavigation.name);
        setLevel(newSubNavigation.level);
        setOrders(newSubNavigation.orders);
    }
}
