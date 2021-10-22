package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;
/*
 * 불량항목 등록 세부 리스트
 * 상위항목 BadItem에 불량항목코드 (0001)
 * 상위항목명 BadItem에 불량항목명 (불량)
 * 순번 (9)
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "BAD_ITEM_DETAILS")
@Data
public class BadItemDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '불량항목 세부 고유아이디'")
    private Long id;

    // 다대일 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAD_ITEM", nullable = false, columnDefinition = "bigint COMMENT '불량항목'")
    private BadItem badItem;    // 불량항목코드, 불량항목명

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '순번'")
    private int orders;     // 순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;  // 삭제여부
}
