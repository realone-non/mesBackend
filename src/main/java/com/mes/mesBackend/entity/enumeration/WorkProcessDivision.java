package com.mes.mesBackend.entity.enumeration;

// 작업공정 구분
/*
* 자재입고: MATERIAL_INPUT
* 원료혼합: MATERIAL_MIXING
* 충진: FILLING
* 캡조립: CAP_ASSEMBLY
* 라벨링: LABELING
* 포장: PACKAGING
* 출하: SHIPMENT
* 재사용 : RECYCLE
* */
public enum WorkProcessDivision {
    MATERIAL_INPUT,
    MATERIAL_MIXING,
    FILLING,
    CAP_ASSEMBLY,
    LABELING,
    PACKAGING,
    SHIPMENT,
    RECYCLE
}
