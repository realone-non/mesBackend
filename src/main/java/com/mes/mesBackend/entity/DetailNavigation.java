package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.response.DetailNavResponse;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEVEL")
    private int level;  // 유저 레벨

    @Column(name = "USE_YN")
    private boolean useYn;  // 사용여부

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SUB_NAVS_ID")
    private SubNavigation subNavigation;

    public void put(DetailNavigation detailNavigation) {
        setName(detailNavigation.name);
        setLevel(detailNavigation.level);
    }
}
