package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "SHIPMENT_LOTS")
@Data
public class ShipmentLot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '출하 lot 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SHIPMENT_ITEM", columnDefinition = "bigint COMMENT '출하 품목정보'", nullable = false)
    private ShipmentItem shipmentItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'lot master'", nullable = false)
    private LotMaster lotMaster;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void create(ShipmentItem shipmentItem, LotMaster lotMaster) {
        setShipmentItem(shipmentItem);
        setLotMaster(lotMaster);
    }



    public void delete() {
        setDeleteYn(true);
    }
}
