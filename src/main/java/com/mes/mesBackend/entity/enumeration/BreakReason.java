package com.mes.mesBackend.entity.enumeration;

// 고장유형
// 17-2
/*
* LIGHT_BREAKDOWN: 경고장
* MIDDLE_FAILURE: 중고장
* FATAL_BREAKDOWN: 치명고장
* [LIGHT_BREAKDOWN: 경고장, MIDDLE_FAILURE: 중고장, FATAL_BREAKDOWN: 치명고장]
* */
public enum BreakReason {
    LIGHT_BREAKDOWN, MIDDLE_FAILURE, FATAL_BREAKDOWN
}
