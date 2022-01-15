package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.ItemLogType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import javax.persistence.*;
import java.time.LocalDate;

//일자별 품목 변동 사항
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_LOG_LIST")
@Data
public class ItemLog extends  BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '일자별 품목 변동 사항 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WAREHOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;

    @Column(name = "LOG_DATE", columnDefinition = "date COMMENT '변동일자'")
    private LocalDate logDate;

    @Column(name = "STORE_AMOUNT", columnDefinition = "int COMMENT '입고수량'")
    private int storeAmount;

    @Column(name = "CREATED_AMOUNT", columnDefinition = "int COMMENT '생산수량'")
    private int createdAmount;

    @Column(name = "BAD_ITEM_AMOUNT", columnDefinition = "int COMMENT '불량수량'")
    private int badItemAmount;

    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '투입수량'")
    private int inputAmount;

    @Column(name = "SHIPMENT_AMOUNT", columnDefinition = "int COMMENT '출하수량'")
    private int shipmentAmount;

    @Column(name = "STOCK_REAL_AMOUNT", columnDefinition = "int COMMENT '재고실사수량'")
    private int stockRealAmount;

    @Column(name = "MOVE_AMOUNT", columnDefinition = "int COMMENT '이동수량'")
    private int moveAmount;

    @Column(name = "RETURN_AMOUNT", columnDefinition = "int COMMENT '반품수량'")
    private int returnAmount;

    @Column(name = "STOCK_AMOUNT", columnDefinition = "int COMMENT '재고수량'")
    private int stockAmount;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    public void update(int amount, ItemLogType logType){
        switch (logType){
            case STORE_AMOUNT:
                setStoreAmount(getStoreAmount() + amount);
                setStockAmount(getStockAmount() + amount);
                break;
            case CREATED_AMOUNT:
                setCreatedAmount(getCreatedAmount() + amount);
                setStockAmount(getStockAmount() + amount);
                break;
            case BAD_AMOUNT:
                setBadItemAmount(getBadItemAmount() + amount);
                setStockAmount(getStockAmount() - amount);
                break;
            case INPUT_AMOUNT:
                setInputAmount(getInputAmount() + amount);
                setStockAmount(getStockAmount() - amount);
                break;
            case SHIPMENT_AMOUNT:
                setShipmentAmount(getShipmentAmount() + amount);
                setStockAmount(getStockAmount() - amount);
                break;
            case RETURN_AMOUNT:
                setReturnAmount(getReturnAmount() + amount);
                setStockAmount(getStockAmount() - amount);
                break;
        }
    }
}
