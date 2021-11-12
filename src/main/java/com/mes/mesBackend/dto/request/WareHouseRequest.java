package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;
import static com.mes.mesBackend.exception.Message.NOT_ZERO;

@Getter
@Setter
@ApiModel(description = "창고")
public class WareHouseRequest {
    @ApiModelProperty(value = "창고코드 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String wareHouseCode;

    @ApiModelProperty(value = "창고명 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String wareHouseName;

    @ApiModelProperty(value = "창고유형 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long wareHouseType;

    @ApiModelProperty(value = "창고그룹")
    String wareHouseGroup;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
