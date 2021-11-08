package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 품목별 검사항목 등록
 * 검색: 공장,검사유형,품목그룹,품목계정
 * 품번
 * 품명
 * 검사유형
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "ITEM_CHECK_CATEGORIES")
@Data
public class ItemCheckCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목별 검사항목 등록 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;      // 품번, 품명

    // enum으로 대체
    @Enumerated(EnumType.STRING)
    @Column(name = "CHECK_CATEGORY", columnDefinition = "varchar(255) COMMENT '검사유형'")
    private TestCategory checkCategory = TestCategory.INPUT_TEST;    // 검사유형

    // enum으로 대체
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '검사타입'")
    private TestType testType = TestType.NO_TEST;              // 검사타입(자동검사,수동검사)
    // 품목등록에서 검사유형,검사타입 다 보여야함

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
