package com.mes.mesBackend.entity.enumeration;

// 결제조건
/*
* 현금: CASH
* 귀사 정기 결제:
* 발주 전 100% 현금:
* 납품 전 100% 현금:
* 등등?
* */
public enum PayType {
    // [현금: CASH]
    // TODO: 쓰이는 곳이 수주와 출하인데 둘의 결제조건이 다른지?
    CASH
}
