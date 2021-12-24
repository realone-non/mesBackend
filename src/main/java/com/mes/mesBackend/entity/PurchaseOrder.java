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
 * 9-2. 구매발주등록
 * 검색: 공장,화폐,담당자,거래처,입고창고,발주기간
 * 발주번호 (20210712-001)
 * 발주일자  (2021.7.12)
 * 거래처 (1128206055)                                      -> Client
 * 거래처명 ((사)대학산업기술지원단)                               -> Client
 * 담당자 (박현정)                                           -> User
 * 입고창고 (본사 자재 창고(본사))                             -> WareHouse
 * 화폐 (KRW￦)                                           -> Currency
 * 환율 (1)
 * 부가세적용 (부가세적용)
 * 납기일자 (2021.7.12)
 * 수주정보 (에어로매스터 / AA01-NC2-M015F)
 * 지시상태 (완료, 진행중)                    -> OrderState
 * 비고
 * 운송조건
 * 별점
 * 운송유형
 * 지불조건
 * Requested(w)
 * 특이사항
 * 구매발주등록
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "PURCHASE_ORDERS")
@Data
public class PurchaseOrder extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매발주정보 고유아이디'")
    private Long id;        // 구매발주정보 고유아이디

    @Column(name = "PURCHASE_ORDER_NO", columnDefinition = "varchar(255) COMMENT '발주번호'", nullable = false)
    private String purchaseOrderNo;     // 발주번호

    @Column(name = "PURCHASE_ORDER_DATE", columnDefinition = "datetime COMMENT '발주일자'", nullable = false)
    private LocalDate purchaseOrderDate;        // 발주일자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'")
    private Client client;              // 거래처

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USERS", columnDefinition = "bigint COMMENT '담당자'", nullable = false)
    private User user;              // 담당자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고'", nullable = false)
    private WareHouse wareHouse;            // 입고창고

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CURRENCY", columnDefinition = "bigint COMMENT '화폐'", nullable = false)
    private Currency currency;              // 화폐

    @Column(name = "SURTAX_YN", columnDefinition = "bit(1) COMMENT '부가세적용 여부'", nullable = false)
    private boolean surtaxYn;                   // 부가세 적용 여부

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                        // 비고

    @Column(name = "TRANSPORT_CONDITION", columnDefinition = "varchar(255) COMMENT '운송조건'")
    private String transportCondition;      // 운송조건

    @Column(name = "ADDENDUM", columnDefinition = "varchar(255) COMMENT '별첨'")
    private String addendum;                // 별첨

    @Column(name = "TRANSPORT_TYPE", columnDefinition = "varchar(255) COMMENT '운송유형'")
    private String transportType;           // 운송유형

    @Column(name = "PAY_CONDITION", columnDefinition = "varchar(255) COMMENT '지불조건'")
    private String payCondition;            // 지불조건

    @Column(name = "REQUESTED_SHIPPING", columnDefinition = "varchar(255) COMMENT 'Requested Shipping(w)'")
    private String requestedShipping;           // Requested Shipping(w)

    @Column(name = "SPECIAL_NOTE", columnDefinition = "varchar(255) COMMENT '특이사항'")
    private String specialNote;                 // 특이사항

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void mapping(
            User user,
            WareHouse wareHouse,
            Currency currency
    ) {
        setUser(user);
        setWareHouse(wareHouse);
        setCurrency(currency);
    }

    public void update(
            PurchaseOrder newPurchaseOrder,
            User newUser,
            WareHouse newWareHouse,
            Currency newCurrency
    ) {
        setPurchaseOrderDate(newPurchaseOrder.purchaseOrderDate);
        setUser(newUser);
        setWareHouse(newWareHouse);
        setCurrency(newCurrency);
        setSurtaxYn(newPurchaseOrder.surtaxYn);
        setNote(newPurchaseOrder.note);
        setTransportCondition(newPurchaseOrder.transportCondition);
        setAddendum(newPurchaseOrder.addendum);
        setTransportType(newPurchaseOrder.transportType);
        setPayCondition(newPurchaseOrder.payCondition);
        setRequestedShipping(newPurchaseOrder.requestedShipping);
        setSpecialNote(newPurchaseOrder.specialNote);
    }

    public void delete() {
        setDeleteYn(true);
        setClient(null);
    }
}
