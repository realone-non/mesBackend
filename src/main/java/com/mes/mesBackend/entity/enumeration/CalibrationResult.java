package com.mes.mesBackend.entity.enumeration;

// 검교정 결과
/*
* PASS: 합격
* FAIL: 불합격
* RETEST: 재검정
* [PASS: 합격, FAIL: 불합격, RETEST: 재검정]
* */
public enum CalibrationResult {
    PASS, FAIL, RETEST
}
