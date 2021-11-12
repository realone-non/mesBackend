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
@ApiModel(description = "품목그룹")
public class ItemGroupResponse {

    @ApiModelProperty(value = "그룹코드")
    CodeResponse.idAndCode itemGroupCode;

    @ApiModelProperty(value = "상위그룹코드")
    String topGroupCode;

    @ApiModelProperty(value = "상위그룹명")
    String topGroupName;

    @ApiModelProperty(value = "그룹명")
    String groupName;

    @ApiModelProperty(value = "기본어명")
    String defaultName;

    @ApiModelProperty(value = "순번")
    int orders;

    @ApiModelProperty(value = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndGroupName {
        Long id;
        String groupName;
    }
}
