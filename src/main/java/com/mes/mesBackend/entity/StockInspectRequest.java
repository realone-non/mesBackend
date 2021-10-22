package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 11-1 재고실사 의뢰 정보 등록
 * 검색: 공장, 실사기간
 * 요청번호
 * 실사일자
 * 비고
 * 재고조사 의뢰 상세 정보: 창고유형,창고,품목그룹,품번,품명
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "STOCK_INSPECT_REQUESTS")
@Data
public class StockInspectRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재고조사 의뢰정보 고유아이디'")
    private Long id;

    @Column(name = "REQUEST_NO",
            nullable = false,
            unique = true,
            columnDefinition = "varchar(255) COMMENT '요청번호'")
    private String requestNo;       // 요청번호

    @Column(name = "INSPECT_DATE",
            nullable = false,
            columnDefinition = "date COMMENT '실사일자'")
    private LocalDate inspectDate;      // 실사일자

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                // 비고

    @Enumerated(EnumType.STRING)
    @Column(name = "STOCK_INSPECT_TYPE", columnDefinition = "bigint COMMENT '재고실사 등록 타입'")
    private StockInspectType stockInspectType = StockInspectType.REQUEST_REGISTER;  // 재고실사 등록 타입

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
