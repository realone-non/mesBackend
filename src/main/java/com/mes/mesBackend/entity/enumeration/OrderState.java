package com.mes.mesBackend.entity.enumeration;

public enum OrderState {
    /*
    * 지시상태
    * 완료: COMPLETION
    * 진행중: ONGOING
    * 예정: SCHEDULE
    * 취소: CANCEL
    * [완료: COMPLETION, 진행중: ONGOING, 예정: SCHEDULE, 취소: CANCEL]
    * */
    COMPLETION, ONGOING, SCHEDULE, CANCEL
}
