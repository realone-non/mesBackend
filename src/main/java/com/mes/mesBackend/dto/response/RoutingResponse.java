package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "라우팅")
public class RoutingResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "라우팅 번호")
    Long routingNo;

    @ApiModelProperty(value = "라우팅 명")
    String routingName;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String routingName;
    }
}
