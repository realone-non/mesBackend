package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "라우팅 상세")
public class RoutingDetailRequest {
    @ApiModelProperty(value = "작업순번")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int orders;

    @ApiModelProperty(value = "작업공정 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workProcess;

    @ApiModelProperty(value = "작업장 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workCenter;

    @ApiModelProperty(value = "검사유형")
    TestCategory testCategory = TestCategory.INPUT_TEST;

    @ApiModelProperty(value = "원자재 창고 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long rawMaterialWareHouse;

    @ApiModelProperty(value = "입고 창고 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long inputWareHouse;

    @ApiModelProperty(value = "메인공정")
    boolean mainProcessYn;

    @ApiModelProperty(value = "최종공정")
    boolean lastProcessYn;

    @ApiModelProperty(value = "작업개시일")
    LocalDate workStartDate;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;
}
