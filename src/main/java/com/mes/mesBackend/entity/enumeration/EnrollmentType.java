package com.mes.mesBackend.entity.enumeration;

public enum EnrollmentType {
    /*
    * 등록유형
    * 불량: ERROR
    * 구매입고: PURCHASE_INPUT
    * 외주입고: OUTSOURCING_INPUT
    * 생산: PRODUCTION
    * 재사용: RECYCLE
    * [불량: ERROR, 구매입고: PURCHASE_INPUT, 생산: PRODUCTION, 외주입고: OUTSOURCING_INPUT]
    * */
    ERROR, PURCHASE_INPUT, OUTSOURCING_INPUT, PRODUCTION, RECYCLE
}
