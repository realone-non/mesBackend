package com.mes.mesBackend.entity;


import com.mes.mesBackend.entity.enumeration.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


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
@NoArgsConstructor(access = PROTECTED)
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

    @Column(name = "CLIENT_MANAGER", columnDefinition = "varchar(255) COMMENT '거래처 담당자'")
    private String clientManager;   // 거래처담당자

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;            // 비고

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @Enumerated(STRING)
    @Column(name = "ORDER_STATE", columnDefinition = "varchar(255) COMMENT '지시상태'", nullable = false)
    private OrderState orderState = SCHEDULE;

    @Column(name = "BARCODE_NUMBER", columnDefinition = "varchar(500) COMMENT '바코드 번호'")
    private String barcodeNumber;

    public void create(Client client, String shipmentNo, String barcodeNumber) {
        setClient(client);
        setOrderState(SCHEDULE);
        setShipmentNo(shipmentNo);
        setBarcodeNumber(barcodeNumber);
    }

    public void update(
            Shipment newShipment
    ) {
        setShipmentDate(newShipment.shipmentDate);
        setClientManager(newShipment.clientManager);
        setNote(newShipment.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
