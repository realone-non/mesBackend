package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* 9-1. 구매요청 등록
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PURCHASE_REQUESTS")
@Data
public class PurchaseRequest extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매요청 고유아이디'")
    private Long id;

    // 제조오더번호
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRODUCE_ORDER", columnDefinition = "bigint COMMENT '제조오더번호'", nullable = false)
    private ProduceOrder produceOrder;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'", nullable = false)
    private Client client;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    // 요청일자
    @Column(name = "REQUEST_DATE", columnDefinition = "datetime(6) COMMENT '요청일자'", nullable = false)
    private LocalDateTime requestDate;

    // 요청수량
    @Column(name = "REQUEST_AMOUNT", columnDefinition = "int COMMENT '요청수량'", nullable = false)
    private int requestAmount;

    // 발주수량
    @Column(name = "ORDER_AMOUNT", columnDefinition = "int COMMENT '발주수량'", nullable = false)
    private int orderAmount;

    // 납기일자
    @Column(name = "PERIOD_DATE", columnDefinition = "datetime(6) COMMENT '납기일자'", nullable = false)
    private LocalDateTime periodDate;

    // 검사유형
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '검사유형'", nullable = false)
    private TestType testType = TestType.NO_TEST;

    // 제조사
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRODUCT_CLIENT", columnDefinition = "bigint COMMENT '제조사'", nullable = false)
    private Client productClient;

    // 비고
    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;
}
