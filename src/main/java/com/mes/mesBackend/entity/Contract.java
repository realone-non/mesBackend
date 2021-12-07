package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.ProductionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수주등록 고유아이디'")
    private Long id;

    @Column(name = "CONTRACT_NO", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '수주번호'")
    private String contractNo;      // 수주번호

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", nullable = false, columnDefinition = "bigint COMMENT '고객사'")
    private Client client;          // 고객사, 고객사명

    @Column(name = "CONTRACT_DATE", columnDefinition = "datetime COMMENT '수주일자'", nullable = false)
    private LocalDateTime contractDate;     // 수주일자

    @Column(name = "CLIENT_ORDER_DATE", columnDefinition = "datetime COMMENT '고객발주일자'", nullable = false)
    private LocalDateTime clientOrderDate;      // 고객발주일자

    @Enumerated(STRING)
    @Column(name = "PRODUCTION_TYPE", columnDefinition = "varchar(255) COMMENT '생산유형'", nullable = false)
    private ProductionType productionType;  // 생산유형

    @Column(name = "CLIENT_ORDER_NO", columnDefinition = "varchar(255) COMMENT '고객발주번호'", nullable = false)
    private String clientOrderNo;       // 고객발주번호

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '담당자'", nullable = false)
    private User user;              // 담당자

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CURRENCY", columnDefinition = "bigint COMMENT '화폐'", nullable = false)
    private Currency currency;          // 화폐

    @Column(name = "SURTAX", columnDefinition = "varchar(255) COMMENT '부가세 적용'", nullable = false)
    private String surtax;              // 부가세적용

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "OUTPUT_WAREHOUSE", columnDefinition = "bigint COMMENT '출고창고'", nullable = false)
    private WareHouse outputWareHouse;         // 출고창고

    @Column(name = "PERIOD_DATE", columnDefinition = "datetime COMMENT '납기일자'", nullable = false)
    private LocalDateTime periodDate;               // 납기일자

    @Column(name = "CHANGE_REASON", columnDefinition = "varchar(255) COMMENT '변경사유'")
    private String changeReason;        // 변경사유

    @Column(name = "PAYMENT_YN", columnDefinition = "bit(1) COMMENT '결제완료'", nullable = false)
    private boolean paymentYn;          // 결제완료

    @Column(name = "PAY_CONDITION", columnDefinition = "varchar(255) COMMENT '지불조건'", nullable = false)
    private String payCondition;        // 지불조건

    @Column(name = "FORWADER", columnDefinition = "varchar(255) COMMENT 'Forwader'", nullable = false)
    private String forwader;                // Forwader

    @Column(name = "TRANSPORT_CONDITION", columnDefinition = "varchar(255) COMMENT '운송조건'", nullable = false)
    private String transportCondition;      // 운송조건

    @Column(name = "SHIPMENT_SERVICE", columnDefinition = "varchar(255) COMMENT 'Shipment Service'", nullable = false)
    private String shipmentService;         // Shipment Service

    @Column(name = "SHIPMENT_WK", columnDefinition = "varchar(255) COMMENT 'Shipment WK'", nullable = false)
    private String shipmentWk;              // Shipment WK

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                    // 비고

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
