package com.mes.mesBackend.entity;


import com.mes.mesBackend.entity.enumeration.PayType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


/*
* 출하 로트 번호는 완제품 기준으로 생성되야함.
* */
/*
 * 4-5. 출하등록
 * 검색: 공장(드롭),거래처(체크),출하기간(캘린더),화폐(체크박스),담당자(체크)
 * 출하번호 (20210706-001)
 * 거래처 (1238171660)              -> Client
 * 거래처명 (단암시스템즈(주))          -> Client
 * 출하일자 (2021.7.6)
 * 담당자 (김경보)                   -> User
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
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '출하등록 고유아이디'")
    private Long id;

    @Column(name = "SHIPMENT_NO", unique = true, columnDefinition = "varchar(255) COMMENT '출하번호'", nullable = false)
    private String shipmentNo;      // 출하번호

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'", nullable = false)
    private Client client;          // 거래처

    @Column(name = "SHIPMENT_DATE", columnDefinition = "date COMMENT '출하일자'")
    private LocalDate shipmentDate; // 출하일자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '담당자'")
    private User user;        // 담당자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '출하창고'")
    private WareHouse wareHouse;    // 출하창고

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CURRENCY", columnDefinition = "bigint COMMENT '화폐'")
    private Currency currency;      // 화폐, 환율

    @Column(name = "CLIENT_MANAGER", columnDefinition = "varchar(255) COMMENT '거래처 담당자'")
    private String clientManager;   // 거래처담당자

    // enum 타입으로 변경
    @Enumerated(STRING)
    @Column(name = "PAY_TYPE", columnDefinition = "varchar(255) COMMENT '결제조건'", nullable = false)
    private PayType payType;    // 결제조건

    // boolean 으로 변경
    @Column(name = "SURTAX", columnDefinition = "bit(1) COMMENT '부가세적용'", nullable = false)
    private boolean surtax;          // 부가세적용

    @Column(name = "TRANSPORT_CONDITION", columnDefinition = "varchar(255) COMMENT '운송조건'")
    private String transportCondition;      // 운송조건

    @Column(name = "FORWADER", columnDefinition = "varchar(255) COMMENT 'Forwader'")
    private String forwader;        // Forwader

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;            // 비고

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "INVOICE", columnDefinition = "bigint COMMENT 'Invoice'")
    private Invoice invoice;        // Invoice

    public void add(Client client, User user, WareHouse wareHouse, Currency currency) {
        setClient(client);
        setUser(user);
        setWareHouse(wareHouse);
        setCurrency(currency);
    }

    public void update(
            Shipment newShipment,
            Client newClient,
            User newUser,
            WareHouse newWareHouse,
            Currency newCurrency
    ) {
        setClient(newClient);
        setShipmentDate(newShipment.shipmentDate);
        setUser(newUser);
        setWareHouse(newWareHouse);
        setCurrency(newCurrency);
        setClientManager(newShipment.clientManager);
        setPayType(newShipment.payType);
        setSurtax(newShipment.surtax);
        setTransportCondition(newShipment.transportCondition);
        setForwader(newShipment.forwader);
        setNote(newShipment.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
