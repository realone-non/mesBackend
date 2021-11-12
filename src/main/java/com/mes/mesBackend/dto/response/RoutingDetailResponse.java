package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "라우팅 상세")
public class RoutingDetailResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "작업순번")
    int orders;

    @ApiModelProperty(value = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @ApiModelProperty(value = "작업장")
    WorkCenterResponse.idAndName workCenter;

    @ApiModelProperty(value = "검사유형")
    String testCategory;

    @ApiModelProperty(value = "원자재 창고")
    WareHouseResponse.idAndName rawMaterialWareHouse;

    @ApiModelProperty(value = "입고 창고")
    WareHouseResponse.idAndName inputWareHouse;

    @ApiModelProperty(value = "메인공정")
    boolean mainProcessYn;

    @ApiModelProperty(value = "최종공정")
    boolean lastProcessYn;

    @ApiModelProperty(value = "작업개시일")
    LocalDate workStartDate;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;
}
