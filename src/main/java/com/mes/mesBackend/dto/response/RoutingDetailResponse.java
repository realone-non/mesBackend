package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoutingDetailResponse {
    Long id;
    int orders;     // 작업순번
    WorkProcessResponse.idAndName workProcess;     // 작업공정
    WorkCenterResponse.idAndName workCenter;       // 작업장
    String testCategory;
    WareHouseResponse.idAndName rawMaterialWareHouse;        // 원자재 창고
    WareHouseResponse.idAndName inputWareHouse;               // 입고 창고 (창고 참고)
    boolean mainProcessYn;          // 메인공정 (예 아니오)
    boolean lastProcessYn;          // 최종공정 (예 아니고)
    LocalDate workStartDate;            // 작업개시일
    boolean useYn;
}
