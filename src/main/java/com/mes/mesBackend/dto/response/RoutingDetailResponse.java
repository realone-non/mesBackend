package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "라우팅 상세")
public class RoutingDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "작업순번")
    int orders;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "작업장")
    WorkCenterResponse.idAndName workCenter;

    @Schema(description = "검사유형")
    String testCategory;

    @Schema(description = "원자재 창고")
    WareHouseResponse.idAndName rawMaterialWareHouse;

    @Schema(description = "입고 창고")
    WareHouseResponse.idAndName inputWareHouse;

    @Schema(description = "메인공정")
    boolean mainProcessYn;

    @Schema(description = "최종공정")
    boolean lastProcessYn;

    @Schema(description = "작업개시일")
    LocalDate workStartDate;

    @Schema(description = "사용여부")
    boolean useYn;
}
