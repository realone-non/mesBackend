package com.mes.mesBackend.entity;

import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '불량항목 등록 고유아이디'")
    private Long id;

    @Column(name = "BAD_ITEM_CODE", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '불량항목코드'")
    private String badItemCode;     //  불량항목코드

    @Column(name = "BAD_ITEM_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '불량항목명'")
    private String badItemName;     // 불량항목명

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;  // 삭제여부
}
