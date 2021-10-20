package com.mes.mesBackend.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 출하반품
 * 검색: 공장(드롭),거래처,품목,반품기간
 * 출하번호                      -> Shipment
 * 거래처                       -> Shipment
 * 거래처명                      -> Shipment
 * 품번                         -> Shipment
 * 품명                          -> Shipment
 * 규격                         -> Shipment
 * LOT번호         -> 미구현
 * LOT유형         -> 미구현
 * 반품일시
 * 반품수량
 * 출하수량                -> Shipment
 * 검사의뢰유형
 * 검사LOT번호               -> 미구현
 * 입고창고                  -> WareHouse
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SHIPMENT_RETURNS")
@Data
public class ShipmentReturn extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "SHIPMENT_ITEM_LIST")
    private Shipment shipmentItemList;              // 출하

    @Column(name = "RETURN_DATE")
    private LocalDate returnDate;       // 반품일시

    @Column(name = "RETURN_AMOUNT")
    private int returnAmount;              // 반품수량

    @Column(name = "TEST_REQUEST_TYPE")
    private String testRequestType;         // 검사의뢰유형

    @OneToOne @JoinColumn(name = "WARE_HOUSE")
    private WareHouse wareHouse;            // 입고창고

    @Column(name = "NOTE")
    private String note;                    // 비고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
