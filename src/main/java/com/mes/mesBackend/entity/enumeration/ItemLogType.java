package com.mes.mesBackend.entity.enumeration;

/*
 * 일자별 품목 변동사항 타입
 * 입고수량:STORE_AMOUNT
 * 생산수량:CREATED_AMOUNT
 * 불량수량:BAD_AMOUNT
 * 투입수량:INPUT_AMOUNT
 * 출하수량:SHIPMENT_AMOUNT
 * 재고실사수량:REAL_AMOUNT
 * 이동수량:MOVE_AMOUNT
 * 반품수량:RETURN_AMOUNT
 * 재고수량:STOCK_AMOUNT
 */
public enum ItemLogType {
    STORE_AMOUNT,
    CREATED_AMOUNT,
    BAD_AMOUNT,
    INPUT_AMOUNT,
    SHIPMENT_AMOUNT,
    REAL_AMOUNT,
    MOVE_AMOUNT,
    RETURN_AMOUNT
}
