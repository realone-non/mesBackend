package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoutingDetailRequest {
    int orders;     // 작업순번
    Long workProcess;     // 작업공정
    Long workCenter;       // 작업장
    TestCategory testCategory = TestCategory.INPUT_TEST;      // 검사유형
    Long rawMaterialWareHouse;        // 원자재 창고
    Long inputWareHouse;               // 입고 창고
    boolean mainProcessYn;          // 메인공정 (예 아니오)
    boolean lastProcessYn;          // 최종공정 (예 아니오)
    LocalDate workStartDate;            // 작업개시일
}
