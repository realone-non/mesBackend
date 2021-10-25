package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 제품검사 상세정보
 * 검사일시
 * 검사수량
 * 양품수량
 * 부적합수량
 * 검사결과
 * 입고창고
 * 검사성적서파일
 * 검사자
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PRODUCT_TEST_DETAILS")
@Data
public class ProductTestDetail extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제품검사 상세정보 고유아이디'")
    private Long id;    // 제품검사 상세정보 고유아이디

    @Column(name = "TEST_DATE", columnDefinition = "date COMMENT '검사일시'")
    private LocalDate testDate; // 검사일시

    @Column(name = "TEST_AMOUNT", columnDefinition = "int COMMENT '검사수량'")
    private int testAmount;     // 검사수량

    @Column(name = "GOOD_PRODUCT_AMOUNT", columnDefinition = "int COMMENT '양품수량'")
    private int goodProductAmount;      // 양품수량

    @Column(name = "BAD_PRODUCT_AMOUNT", columnDefinition = "int COMMENT '부적합수량'")
    private int badProductAmount;       // 부적합수량

    @Column(name = "TEST_RESULT", columnDefinition = "varchar(255) COMMENT '검사결과'")
    private String testResult;          // 검사결과

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '입고창고'")
    private WareHouse wareHouse;        // 입고창고

    @Column(name = "TEST_RESULT_FILE_URL", columnDefinition = "varchar(255) COMMENT '검사성적서 파일'" )
    private String testResultFileUrl;       // 검사성적서 파일

    @Column(name = "TESTER", columnDefinition = "varchar(255) COMMENT '검사자'")
    private String tester;          // 검사자

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;            // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_TEST", columnDefinition = "bigint COMMENT '검사의뢰 정보'")
    private ProductTest productTest;        // 검사의뢰 등록
}
