package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 불량항목 등록
 * 검색: 불량유형
 * 불량항목코드 (0001)
 * 불량항목명 (불량,재작업,수리)
 * // 불량항목 -> 디테일 불량항목
 * 상위항목 (ROOT,1000)
 * 상위항목명 (ROOT,불량)
 * 순번 9
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "BAD_ITEMS")
@Data
public class BadItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @Column(name = "BAD_ITEM_CODE", nullable = false, unique = true)
    private String badItemCode;     //  불량항목코드

    @Column(name = "BAD_ITEM_NAME", nullable = false)
    private String badItemName;     // 불량항목명

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
