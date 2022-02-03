package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PUBLIC;

//재사용 생성 목록
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "RECYCLE_ITEMS")
@Data
public class RecycleItem extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재사용 생성 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'LOT마스터'")
    private LotMaster lotMaster;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RECYCLE", columnDefinition = "bigint COMMENT '재사용 유형'")
    private Recycle recycle;

    @Column(name = "RECYCLE_AMOUNT", columnDefinition = "bigint COMMENT '재사용 수량'")
    private int recycleAmount;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;
}
