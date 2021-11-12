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
@ApiModel(description = "공장")
public class FactoryResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "공장코드")
    String factoryCode;     // 공장코드

    @ApiModelProperty(value = "공장명")
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

    @ApiModelProperty(value = "LOT용 코드")
    String lotCode;         // LOT용 코드

    @ApiModelProperty(value = "사용여부")
    boolean useYn;           // 사용

    @ApiModelProperty(value = "사업장")
    String workPlaceName;   // 사업장
}
