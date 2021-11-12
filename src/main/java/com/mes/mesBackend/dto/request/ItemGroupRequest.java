package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "품목그룹")
public class ItemGroupRequest {

    @ApiModelProperty(value = "그룹코드 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemGroupCode;

    @ApiModelProperty(value = "상위그룹코드")
    String topGroupCode;        // 상위그룹코드

    @ApiModelProperty(value = "상위그룹명")
    String topGroupName;        // 상위그룹명

    @ApiModelProperty(value = "그룹명")
    String groupName;           // 그룹명

    @ApiModelProperty(value = "기본어명")
    String defaultName;         // 기본어명

    @ApiModelProperty(value = "순번")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;              // 순번

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
