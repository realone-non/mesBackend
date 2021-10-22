package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 외주입고 등록
 * 검색: 공장,외주처,LOT입고단위수량,품목,입고기간
 * 외주처
 * 생산요청번호
 * 생산품번
 * 생산품명
 * 입고일시
 * 미입고수량
 * 입고수량
 * 입고창고
 * 검사의뢰유형
 * 비고
 * 외주 입고 LOT정보 -> 미구현
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OUT_SOURCING_INPUT")
@Data
public class OutSourcingInput extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주입고 등록 고유아이디'")
    private Long id;

    @Column(name = "PRODUCTION_REQEUST_NO", nullable = false, columnDefinition = "bigint COMMENT '생산요청번호'")
    private String productionRequestNo;         // 생산요청번호

    @Column(name = "INPUT_DATE", nullable = false, columnDefinition = "bigint COMMENT '입고일시'")
    private LocalDate inputDate;                // 입고일시

    @Column(name = "NO_INPUT_AMOUNT", columnDefinition = "bigint COMMENT '미입고수량'")
    private int noInputAmount;                  // 미입고수량

    @Column(name = "INPUT_AMOUNT", columnDefinition = "bigint COMMENT '입고수량'")
    private int inputAmount;                    // 입고수량

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INPUT_WARE_HOUSE", columnDefinition = "bigint COMMENT '입고창고'")
    private WareHouse inputWareHouse;           // 입고창고

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_REQUEST_TYPE", columnDefinition = "bigint COMMENT '검사의뢰유향'")
    private TestType testRequestType;                  // 검사의뢰유형

    @Column(name = "NOTE", columnDefinition = "bigint COMMENT '비고'")
    private String note;                        // 비고

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_SOURCING_PRODUCTION_REQUEST", columnDefinition = "bigint COMMENT '외주 생산 의뢰'")
    private OutSourcingProductionRequest outSourcingProductionRequest;      // 외주 생산 의뢰 등록

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
