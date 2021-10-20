package com.mes.mesBackend.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 출하등록
 * 검색: 공장(드롭),거래처(체크),출하기간(캘린더),화폐(체크박스),담당자(체크)
 * 출하번호 (20210706-001)
 * 거래처 (1238171660)              -> Client
 * 거래처명 (단암시스템즈(주))          -> Client
 * 출하일자 (2021.7.6)
 * 담당자 (김경보)                   -> Manager
 * 출하창고 (본사 완제품 창고(본사))      -> WareHouse
 * 화폐 (KRW￦)                     -> Currency
 * 환율 (1)
 * 거래처담당자 ()
 * 걸제조건 (현금)
 * 부가세적용 (부가세적용)
 * 운송조건 ()
 * Forwader ()
 * 비고 ()
 * 품목정보: 수주번호,품번,품명,규격,수주단위,수주미출하수량,출하수량,출하금액,출하금액(원화),재고수량,PKGS,N/W(KG),G/W(KG),비고
 * LOT정보: LOT번호,수주단위,출고수량,출하금액,출하금액(원화),VAT,검사번호        -> 미구현
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SHIPMENTS")
@Data
public class Shipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "SHIPMENT_NO", nullable = false, unique = true)
    private String shipmentNo;      // 출하번호

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CLIENT", nullable = false)
    private Client client;          // 거래처

    @Column(name = "SHIPMENT_DATE", nullable = false)
    private LocalDate shipmentDate; // 출하일자

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "MANAGER")
    private Manager manager;        // 담당자

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "WARE_HOUSE")
    private WareHouse wareHouse;    // 창고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CURRENCY", nullable = false)
    private Currency currency;      // 화폐

    @Column(name = "EXCHANGE_RATE", nullable = false)
    private int exchangeRate;       // 환율

    @Column(name = "CLIENT_MANAGER")
    private String clientManager;   // 거래처담당자

    @Column(name = "PAY_CONDITION")
    private String payCondition;    // 결제조건

    @Column(name = "SURTAX", nullable = false)
    private String surtax;          // 부가세적용

    @Column(name = "TRANSPORT_CONDITION")
    private String transportCondition;      // 운송조건

    @Column(name = "FORWADER")
    private String forwader;        // Forwader

    @Column(name = "NOTE")
    private String note;            // 비고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "INVOICE")
    private Invoice invoice;        // Invoice
}
