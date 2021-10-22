package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MAIN_NAVS")
@Data
public class MainNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '메인 네비게이션 고유아이디'")
    private Long id;

    @Column(name = "NAME", unique = true, columnDefinition = "bigint COMMENT '네비게이션 명'")
    private String name;

    @Column(name = "LEVEL", columnDefinition = "bigint COMMENT '유저레벨'")
    private int level;  // 유저 레벨

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;  // 사용여부

    @Column(name = "ORDERS", unique = true, columnDefinition = "bigint COMMENT '출력순번'")
    private int orders;     // 출력순번

    public void put(MainNavigation newMainNavigation) {
        setName(newMainNavigation.name);
        setLevel(newMainNavigation.level);
        setOrders(newMainNavigation.orders);
    }
}
