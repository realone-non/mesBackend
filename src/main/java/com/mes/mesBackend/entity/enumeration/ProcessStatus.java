package com.mes.mesBackend.entity.enumeration;

public enum ProcessStatus {
    /*
    * 원자재 등록: MATERIAL_REGISTRATION
    * 중간 검사: MIDDLE_TEST
    * 로트 분할: LOT_DIVIDE
    * [원자재 등록: MATERIAL_REGISTRATION, 중간 검사: MIDDLE_TEST, 로트 분할: LOT_DIVIDE]
    * */
    MATERIAL_REGISTRATION, MIDDLE_TEST, LOT_DIVIDE
}
