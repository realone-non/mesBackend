package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "GRID_OPTIONS")
public class GridOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'grid option 고유아이디'")
    private Long id;

    // 미구현
    @Column(name = "USER_ID", columnDefinition = "varchar(255) COMMENT '사용자 아이디'")
    private String userId;        // 사용자 아이디

    @Column(name = "AGG_FUNC", columnDefinition = "varchar(255) COMMENT 'aggFunc'")
    private String aggFunc;

    @Column(name = "FLEX", columnDefinition = "varchar(255) COMMENT 'flex'")
    private String flex;

    @Column(name = "HIDE", columnDefinition = "bit(1) COMMENT 'hide'")
    private boolean hide;

    @Column(name = "PINNED", columnDefinition = "varchar(255) COMMENT 'pinned'")
    private String pinned;

    @Column(name = "PIVOT", columnDefinition = "bit(1) COMMENT 'pinned'")
    private boolean pivot;

    @Column(name = "PINNET_INDEX", columnDefinition = "varchar(255) COMMENT 'pivotIndex'")
    private String pivotIndex;

    @Column(name = "ROW_GROUP", columnDefinition = "bit(1) COMMENT 'rowGroup'")
    private boolean rowGroup;

    @Column(name = "ROW_GROUPT_INDEX", columnDefinition = "varchar(255) COMMENT 'rowGroupIndex'")
    private String rowGroupIndex;

    @Column(name = "SORT", columnDefinition = "varchar(255) COMMENT 'sort'")
    private String sort;

    @Column(name = "SORT_INDEX", columnDefinition = "varchar(255) COMMENT 'sortIndex'")
    private String sortIndex;

    @Column(name = "WIDTH", columnDefinition = "varchar(255) COMMENT 'width'")
    private String width;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "HEADER", columnDefinition = "bigint COMMENT '헤더정보'")
    private Header header;
}
