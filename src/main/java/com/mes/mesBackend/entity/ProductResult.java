package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* 8-6. 생산실적 관리
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PRODUCT_RESULTS")
@Data
public class ProductResult extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '생산실적관리 고유아이디'")
    private Long id;

    // 수주 -> 수주번호
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT", columnDefinition = "bigint COMMENT '수주정보'", nullable = false)
    private Contract contract;

    // 제조오더번호
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRODUCE_ORDER", columnDefinition = "bigint COMMENT '제조오더번호'", nullable = false)
    private ProduceOrder produceOrder;

    // 거래처
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'", nullable = false)
    private Client client;

    // 품목 -> 모델
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITME", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    // 납기일
    @Column(name = "PERIOD_DATE", columnDefinition = "datetime(6) COMMENT '납기일'", nullable = false)
    private LocalDateTime periodDate;

    // 담당자
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '담당자'", nullable = false)
    private User user;

    // 수주 품목 -> 수주량, 단가
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT_ITEM", columnDefinition = "bigint COMMENT '수주품목'", nullable = false)
    private ContractItem contractAmount;

    // default value 0
    // 생산량
    @Column(name = "PRODUCTION", columnDefinition = "int COMMENT '생산량'", nullable = false)
    private int production;

    // 구분
    @Column(name = "TYPE", columnDefinition = "bit(1) COMMENT '구분'", nullable = false)
    private boolean type;

    // 수입검사
    @Column(name = "PROCESS_01", columnDefinition = "datetime(6) COMMENT '수입검사'")
    private LocalDateTime process01;

    // 원료혼합
    @Column(name = "PROCESS_02", columnDefinition = "datetime(6) COMMENT '원료혼합'")
    private LocalDateTime process02;

    // 충진
    @Column(name = "PROCESS_03", columnDefinition = "datetime(6) COMMENT '충진'")
    private LocalDateTime process03;

    // 앰플삽입
    @Column(name = "PROCESS_04", columnDefinition = "datetime(6) COMMENT '앰플삽입'")
    private LocalDateTime process04;

    // 캠조립 및 융착
    @Column(name = "PROCESS_05", columnDefinition = "datetime(6) COMMENT '캠조립 및 융착'")
    private LocalDateTime process05;

    // 라벨링
    @Column(name = "PROCESS_06", columnDefinition = "datetime(6) COMMENT '라벨링'")
    private LocalDateTime process06;

    // 포장
    @Column(name = "PROCESS_07", columnDefinition = "datetime(6) COMMENT '포장'")
    private LocalDateTime process07;

    // 출하검사
    @Column(name = "PROCESS_08", columnDefinition = "datetime(6) COMMENT '출하검사'")
    private LocalDateTime process08;

    // 포장
    @Column(name = "PROCESS_09", columnDefinition = "datetime(6) COMMENT '포장'")
    private LocalDateTime process09;

    // 출하
    @Column(name = "PROCESS_10", columnDefinition = "datetime(6) COMMENT '출하'")
    private LocalDateTime process10;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn;
}
