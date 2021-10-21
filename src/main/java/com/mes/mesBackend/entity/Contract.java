package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
/*
 * 수주등록
 * 검색: 공장(드롭), 거래처(체크),수주기간(캘린더),화폐(체크),담당자(체크),구분(체크)
 * 수주번호 (20210719-001)
 * 고객사 (1078606987)          -> Client
 * 고객사명 (에어로매스터)         -> Client
 * 수주일자 (2021.7.19)
 * 고객발주일자 (2021.7.19)
 * 생산유형 (양산)
 * 고객발주번호 (AD-P2-210712-004)
 * 담당자 (오석진)                       -> Manager
 * 화폐 (KRW￦)                         -> Currency
 * 부가세적용 (부가세적용)
 * 출고창고 (본사 완제품 창고(본사))          -> WareHouse
 * 납기일자 (2021.10.5)
 * 변경사유 ()
 * 결재완료 (Y,N)
 * 지불조건 (현금)
 * Forwarder ()
 * 운송조건 ()
 * shipment Service ()
 * Shipment WK ()
 * 비고 ()
 * 해당하는 품번들이 있음 -> 품번,품명,규격,수주단위,수주수량,단가,수주금액,수주금액,부가세,수주유형,납기일자,고객발주번호,규격화 품번,비고,첨부파일
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CONTRACTS")
@Data
public class Contract extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "CONTRACT_NO", nullable = false, unique = true)
    private String contractNo;      // 수주번호

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CLIENT", nullable = false)
    private Client client;          // 고객사

    @Column(name = "CONTRACT_DATE")
    private LocalDate contractDate;     // 수주날짜

    @Column(name = "CLIENT_ORDER_DATE")
    private LocalDate clientOrderDate;      // 고객발주일자

    @Column(name = "PRODUCTION_TYPE")
    private String productionType;      // 생산유형

    @Column(name = "CLIENT_ORDER_NO")
    private String clientOrderNo;       // 고객발주번호

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "MANAGER")
    private Manager manager;              // 담당자

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CURRENCY")
    private Currency currency;          // 화폐

    @Column(name = "SURTAX")
    private String surtax;              // 부가세적용

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "OUTPUT_WAREHOUSE")
    private WareHouse outputWareHouse;         // 출고창고

    @Column(name = "PERIOD_DATE")
    private LocalDate periodDate;               // 납기일자

    @Column(name = "CHANGE_REASON")
    private boolean changeReason;        // 변경사유

    @Column(name = "PAY_CONDITION")
    private String payCondition;        // 지불조건

    @Column(name = "FORWADER")
    private String forwader;                // Forwader

    @Column(name = "TRANSPORT_CONDITION")
    private String transportCondition;      // 운송조건

    @Column(name = "SHIPMENT_SERVICE")
    private String shipmentService;         // Shipment Service

    @Column(name = "SHIPMENT_WK")
    private String shipmentWk;              // Shipment WK

    @Column(name = "NOTE")
    private String note;                    // 비고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}