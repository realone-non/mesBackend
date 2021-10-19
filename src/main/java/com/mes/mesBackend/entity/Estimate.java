package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
/*
 * 견적등록
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ESTIMATES")
@Data
public class Estimate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "ESTIMATE_NO", nullable = false, unique = true)
    private String estimateNo;  // 견적번호

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CLIENT", nullable = false)
    private Client client;      // 거래처

    @OneToOne @JoinColumn(name = "MANAGER", nullable = false)
    private Manager manager;      // 담당자

    @Column(name = "ESTIMATE_DATE", nullable = false)
    private LocalDate estimateDate;     // 견적일자

    @OneToOne @JoinColumn(name = "CURRENCY")
    private Currency currency;            // 화폐

    @Column(name = "PERIOD")
    private String period;              // 납기

    @Column(name = "VALIDITY", nullable = false)
    private int validity;               // 유효기간

    @Column(name = "PAY_CONDITION")
    private String payCondition;        // 지불조건

    @Column(name = "SURTAX")
    private String surtax;              // 부가세

    @Column(name = "TRANSPORT_CONDITION")
    private String transportCondition;      // 운송조건

    @Column(name = "FORWADER")
    private String forwader;                // Forwader

    @Column(name = "DESTINATION")
    private String destination;             // DESTINATION

    @OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "ESTIMATE_ITEM")
    private List<EstimateItemList> estimateItemLists;       // 해당하는 품목정보

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "PI")
    private Pi pi;              // P/I
}
