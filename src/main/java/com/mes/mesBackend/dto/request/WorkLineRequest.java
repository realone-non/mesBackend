package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkLineRequest {
    Long workLineCode;           // 라인코드
    String workLineName;          // 작업라인명
    Long workCenter;             // 작업장
//    Long wareHouse;            // 원자재 창고
    Long workProcess;            // 작업공정
    String popStartFormid;      // POP 시작 FORMID
//    Long client;              // 외주사
    int time;                   // 일 가동시간
    boolean useYn;
}
