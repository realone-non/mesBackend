package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 4-1. 견적등록
 * 검색: 공장(드롭),거래처(체크),견적기간(캘린더),화폐,담당자
 * 견적번호 (20210714-001)
 * 거래처 (1238152076)                      -> Client
 * 거래처명 ((주)이엠시스)                      -> Client
 * 담당자 (오석진)
 * 견적일자 (2021.7.14)
 * 화폐 (KRW￦)                              -> Currency
 * 납기 (발주후 6주이내)
 * 거래처담당자 ()                         -> Client
 * 유효기간 (90일)
 * 지불조건 (현금)
 * 부가세적용 (부가세적용)
 * 운송조건 ()
 * Forwader ()
 * DESTINATION ()
 * 비고 ()
 * 견적에 해당하는 품목정보:품번,품명,규격,단위,수량,단가,금액,비고    -> EstimateItemList
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "ESTIMATES")
@Data
public class Estimate extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '견적등록 고유아이디'")
    private Long id;

    @Column(name = "ESTIMATE_NO", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '견적번호'")
    private String estimateNo;  // 견적번호

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", nullable = false, columnDefinition = "bigint COMMENT '거래처'")
    private Client client;      // 거래처

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", nullable = false, columnDefinition = "bigint COMMENT '직원'")
    private User user;          // 담당자

    @Column(name = "ESTIMATE_DATE", nullable = false, columnDefinition = "date COMMENT '견적일자'")
    private LocalDate estimateDate;     // 견적일자

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CURRENCY", columnDefinition = "bigint COMMENT '화폐'", nullable = false)
    private Currency currency;            // 화폐

    @Column(name = "PERIOD", columnDefinition = "varchar(255) COMMENT '납기'", nullable = false)
    private String period;              // 납기

    @Column(name = "VALIDITY", nullable = false, columnDefinition = "int COMMENT '유효기간'")
    private int validity;               // 유효기간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PAY_TYPE", columnDefinition = "bigint COMMENT '지불조건'", nullable = false)
    private PayType payType;        // 지불조건

    @Column(name = "SURTAX", columnDefinition = "varchar(255) COMMENT '부가세'", nullable = false)
    private String surtax;              // 부가세

    @Column(name = "TRANSPORT_CONDITION", columnDefinition = "varchar(255) COMMENT '운송조건'")
    private String transportCondition;      // 운송조건

    @Column(name = "FORWADER", columnDefinition = "varchar(255) COMMENT 'Forwader'")
    private String forwader;                // Forwader

    @Column(name = "DESTINATION", columnDefinition = "varchar(255) COMMENT 'DESTINATION'")
    private String destination;             // DESTINATION

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "PI", columnDefinition = "bigint COMMENT 'PI'")
    private Pi pi;              // P/I

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    public void addMapping(Client client, Currency currency, String estimateNo, User user, PayType payType) {
        setClient(client);
        setCurrency(currency);
        // 견적번호 생성
        setEstimateNo(estimateNo);
        setUser(user);
        setPayType(payType);
    }

    public void update(Client newClient, Currency newCurrency, Estimate newEstimate, User newUser, PayType newPayType) {
        setClient(newClient);
        setEstimateDate(newEstimate.estimateDate);
        setCurrency(newCurrency);
        setPeriod(newEstimate.period);
        setValidity(newEstimate.validity);
        setPayType(newPayType);
        setSurtax(newEstimate.surtax);
        setTransportCondition(newEstimate.transportCondition);
        setForwader(newEstimate.forwader);
        setDestination(newEstimate.destination);
        setUser(newUser);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void addPi(Pi pi) {
        setPi(pi);
    }
}
