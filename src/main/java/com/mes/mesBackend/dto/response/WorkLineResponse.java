package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class WorkLineResponse {
    Long id;
    CodeResponse.idAndCode workLineCode;              // 라인코드
    String workLineName;                    // 작업라인명
    WorkCenterResponse.idAndName workCenter;          // 작업장
//    WareHouseResponse.idAndName wareHouse;            // 원자재 창고
    WorkProcessResponse.idAndName workProcess;        // 작업공정
    String popStartFormid;                  // POP 시작 FORMID
//    ClientNameResponse client;              // 외주사
    int time;                               // 일 가동시간
    boolean useYn;
}
