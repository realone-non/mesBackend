package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 출하등록에 품목정보
 * 수주번호 (20210604-001)           -> Contract
 * 품번 (AA01-EP2-A005D)            -> Contract
 * 품명 (EMI FILTER)                -> Contract
 * 규격 (EP2-A005D)                -> Contract
 * 수주단위 (개)                      -> Contract
 * 수주미출하수량 (0)
 * 출하수량 (6)
 * 출하금액 (8,400,000.00)
 * 출하금액(원화) (8,400,000)
 * 재고수량 (0)
 * PKGS ()
 * N/W(KG) (0.0)
 * G/W(KG) (0.0)
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SHIPMENT_ITEMS")
@Data
public class ShipmentItems extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '출하등록 품목정보 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT", nullable = false, columnDefinition = "bigint COMMENT '수주'")
    private Contract contract;      // 수주

    @Column(name = "NOT_SHIPPED_AMOUNT", nullable = false, columnDefinition = "int COMMENT '수주 미출하수량'")
    private int notShippedAmount;   // 수주미출하수량

    @Column(name = "SHIPMENT_AMOUNT", nullable = false, columnDefinition = "int COMMENT '출하수량'")
    private int shipmentAmount;     // 출하수량

    @Column(name = "SHIPMENT_PRICE", nullable = false, columnDefinition = "int COMMENT '출하금액'")
    private int shipmentPrice;      // 출하금액

    @Column(name = "SHIPMENT_PRICE_WON", nullable = false, columnDefinition = "int COMMENT '출하금액(원화)'")
    private int shipmentPriceWon;   // 출하금액(원화)

    @Column(name = "INVENTORY_AMOUNT", nullable = false, columnDefinition = "int COMMENT '재고수량'")
    private int inventoryAMOUNT;          // 재고수량

    @Column(name = "PKGS", columnDefinition = "float COMMENT 'PKGS'")
    private float pkgs;                // PKGS

    @Column(name = "NW", columnDefinition = "float COMMENT 'NW'")
    private float nw;                 // N/W(KG)

    @Column(name = "GW", columnDefinition = "float COMMENT 'GW'")
    private float gw;                 // G/W(KG)

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;            // 비고

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPMENT", columnDefinition = "bigint COMMENT '출하'")
    private Shipment shipment;          // 출하
}
