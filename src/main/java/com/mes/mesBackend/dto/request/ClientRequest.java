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
@ApiModel(description = "거래처")
public class ClientRequest {

    @ApiModelProperty(value = "거래처코드 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String clientCode;

    @ApiModelProperty(value = "거래처명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String name;

    @ApiModelProperty(value = "약어")
    String shortName;

    @ApiModelProperty(value = "거래처 유형 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long clientType;

    @ApiModelProperty(value = "사업자등록번호 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String businessNumber;

    @ApiModelProperty(value = "사업자등록증")
    String businessFile;

    @ApiModelProperty(value = "대표자명 NOT NULL")
    @NotBlank(message = NOT_NULL)
    String ceoName;

    @ApiModelProperty(value = "우편번호")
    String postalCode;

    @ApiModelProperty(value = "주소")
    String address;

    @ApiModelProperty(value = "상세주소")
    String detailAddress;

    @ApiModelProperty(value = "업태 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long businessType;

    @ApiModelProperty(value = "업종")
    String item;

    @ApiModelProperty(value = "전화번호")
    String telNumber;

    @ApiModelProperty(value = "fax번호")
    String faxNumber;

    @ApiModelProperty(value = "홈페이지")
    String homePageUrl;

    @ApiModelProperty(value = "메일")
    String mail;

    @ApiModelProperty(value = "담당자 이름")
    String clientChargeName;

    @ApiModelProperty(value = "무역업등록번호")
    String tradeBusinessRegNo;

    @ApiModelProperty(value = "통관고유번호")
    String pccc;

    @ApiModelProperty(value = "국가코드 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long countryCode;

    @ApiModelProperty(value = "지역")
    String area;

    @ApiModelProperty(value = "거래화폐단위")
    String currencyUnit;

    @ApiModelProperty(value = "회사담당자")
    String companyCharge;

    @ApiModelProperty(value = "회사담당부서")
    String companyChargeDept;

    @ApiModelProperty(value = "대금결제방법")
    String paymentMethod;

    @ApiModelProperty(value = "결제일자")
    String paymentDate;

    @ApiModelProperty(value = "운송방법")
    String transitMethod;

    @ApiModelProperty(value = "검사방법")
    String testMethod;

    @ApiModelProperty(value = "검색어")
    String searchWord;

    @ApiModelProperty(value = "휴대폰번호")
    String phoneNumber;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
