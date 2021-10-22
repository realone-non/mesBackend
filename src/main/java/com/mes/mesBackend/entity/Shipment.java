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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '출하등록 고유아이디'")
    private Long id;

    @Column(name = "SHIPMENT_NO", nullable = false, unique = true, columnDefinition = "bigint COMMENT '출하번호'")
    private String shipmentNo;      // 출하번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", nullable = false, columnDefinition = "bigint COMMENT '거래처'")
    private Client client;          // 거래처

    @Column(name = "SHIPMENT_DATE", nullable = false, columnDefinition = "bigint COMMENT '출하일자'")
    private LocalDate shipmentDate; // 출하일자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER", columnDefinition = "bigint COMMENT '담당자'")
    private Manager manager;        // 담당자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;    // 창고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CURRENCY", nullable = false, columnDefinition = "bigint COMMENT '화폐'")
    private Currency currency;      // 화폐

    @Column(name = "EXCHANGE_RATE", nullable = false, columnDefinition = "bigint COMMENT '환율'")
    private int exchangeRate;       // 환율

    @Column(name = "CLIENT_MANAGER", columnDefinition = "bigint COMMENT '거래처 담당자'")
    private String clientManager;   // 거래처담당자

    @Column(name = "PAY_CONDITION", columnDefinition = "bigint COMMENT '결제조건'")
    private String payCondition;    // 결제조건

    @Column(name = "SURTAX", nullable = false, columnDefinition = "bigint COMMENT '부가세적용'")
    private String surtax;          // 부가세적용

    @Column(name = "TRANSPORT_CONDITION", columnDefinition = "bigint COMMENT '운송조건'")
    private String transportCondition;      // 운송조건

    @Column(name = "FORWADER", columnDefinition = "bigint COMMENT 'Forwader'")
    private String forwader;        // Forwader

    @Column(name = "NOTE", columnDefinition = "bigint COMMENT '비고'")
    private String note;            // 비고

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '창고'")
    private Factory factory;                // 공장

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "INVOICE", columnDefinition = "bigint COMMENT 'Invoice'")
    private Invoice invoice;        // Invoice
}
