package com.mes.mesBackend.entity.enumeration;

public enum PurchaseState {
    // 구매요청, 발주, 구매완료, 입고완료, 반품등록, 반품완료
    PURCHASE_REQUEST, ORDER, PURCHASE_COMPLETED, INPUT_COMPLETED, RETURN_REQUEST, RETURN_COMPLETED
    /*
    * 구매요청: PURCHASE_REQUEST
    * 발주: ORDER
    * 구매완료: PURCHASE_COMPLETED
    * 입고완료: INPUT_COMPLETED
    * 반품등록: RETURN_REQUEST
    * 반품완료: RETURN_COMPLETED
    * */
}
