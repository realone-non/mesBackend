package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "사업장")
public class WorkPlaceResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "사업장 코드")
    String workPlaceCode;   // 사업장 코드

    @ApiModelProperty(value = "사업장명")
    String workPlaceName;   // 사업장명

    @ApiModelProperty(value = "사업자등록번호")
    String businessRegNo;  // 사업자등록번호

    @ApiModelProperty(value = "대표자명")
    String ceoName;     // 대표자명

    @ApiModelProperty(value = "우편번호")
    String postalCode;      // 우편번호

    @ApiModelProperty(value = "주소")
    String address;    // 주소

    @ApiModelProperty(value = "상세주소")
    String detailAddress;    // 상세주소

    @ApiModelProperty(value = "영문주소1")
    String engAddress1;     // 영문주소1

    @ApiModelProperty(value = "영문주소2")
    String engAddress2;    // 영문주소2

    @ApiModelProperty(value = "업태")
    List<BusinessTypeResponse> type;  // 업태

    @ApiModelProperty(value = "업종")
    String item;    // 업종

    @ApiModelProperty(value = "전화번호")
    String telNumber;  // 전화번호

    @ApiModelProperty(value = "fax번호")
    String faxNumber;   // fax번호

    @ApiModelProperty(value = "법인코드")
    String corporateCode;   // 법인코드

    @ApiModelProperty(value = "사용여부")
    boolean useYn;           // 사용여부
}
