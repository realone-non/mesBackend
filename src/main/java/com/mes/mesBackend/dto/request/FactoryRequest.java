package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@ApiModel(description = "공장")
public class FactoryRequest {

    @ApiModelProperty(value = "공장코드 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String factoryCode;     // 공장코드

    @ApiModelProperty(value = "공장명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String factoryName;     // 공장명

    @ApiModelProperty(value = "약어")
    String shortName;       // 약어

    @ApiModelProperty(value = "우편번호")
    String postalCode;      // 우편번호

    @ApiModelProperty(value = "기본주소")
    String address;         // 기본주소

    @ApiModelProperty(value = "상세주소")
    String detailAddress;   // 상세주소

    @ApiModelProperty(value = "영문주소1")
    String engAddress1;     // 영문주소1

    @ApiModelProperty(value = "영문주소2")
    String engAddress2;     // 영문주소2

    @ApiModelProperty(value = "전화번호")
    String telNumber;       // 전화번호

    @ApiModelProperty(value = "fax번호")
    String faxNumber;       // fax번호

    @ApiModelProperty(value = "LOT용 코드 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String lotCode;         // LOT용 코드

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;           // 사용

    @ApiModelProperty(value = "사업장 id NOT NULL")
    @NotNull(message = NOT_NULL)
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long workPlaceId;       // 사업장
}
