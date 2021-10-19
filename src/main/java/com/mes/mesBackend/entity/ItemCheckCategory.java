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
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "ITEM")
    private Item item;      // 품번, 품명

    @OneToOne @JoinColumn(name = "CHECK_CATEGORY")
    private TestCategory checkCategory;    // 검사유형

    @OneToOne @JoinColumn(name = "TEST_TYPE")
    private TestType testType;              // 검사타입(자동검사,수동검사)
    // 품목등록에서 검사유형,검사타입 다 보여야함

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne @JoinColumn(name = "FACTORY")
    private Factory factory;                // 공장
}
