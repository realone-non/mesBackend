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
@ApiModel(description = "거래처")
public class ClientResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "거래처코드")
    String clientCode;

    @ApiModelProperty(value = "거래처명")
    String name;    // 거래처명

    @ApiModelProperty(value = "약어")
    String shortName;      // 약어

    @ApiModelProperty(value = "거래처 유형")
    ClientTypeResponse clientType;   // 거래처 유형

    @ApiModelProperty(value = "사업자등록번호")
    String businessNumber;  // 사업자등록번호

    @ApiModelProperty(value = "사업자등록증")
    String businessFile;    // 사업자등록증

    @ApiModelProperty(value = "대표자명")
    String ceoName;     // 대표자명

    @ApiModelProperty(value = "우편번호")
    String postalCode;      // 우편번호

    @ApiModelProperty(value = "주소")
    String address;     // 주소

    @ApiModelProperty(value = "상세주소")
    String detailAddress;   // 상세주소

    @ApiModelProperty(value = "업태")
    BusinessTypeResponse businessType;      // 업태

    @ApiModelProperty(value = "업종")
    String item;        // 업종

    @ApiModelProperty(value = "전화번호")
    String telNumber;   // 전화번호

    @ApiModelProperty(value = "fax번호")
    String faxNumber;   // fax번호 12자

    @ApiModelProperty(value = "홈페이지")
    String homePageUrl; //  홈페이지 주소

    @ApiModelProperty(value = "메일")
    String mail;        // 메일

    @ApiModelProperty(value = "담당자 이름")
    String clientChargeName;  // 담당자 이름

    @ApiModelProperty(value = "무역업등록번호")
    String tradeBusinessRegNo;  // 무역업등록번호

    @ApiModelProperty(value = "통관고유번호")
    String pccc;            // 통관고유번호

    @ApiModelProperty(value = "국가코드")
    CountryCodeResponse countryCode;     // 국가코드

    @ApiModelProperty(value = "지역")
    String area;        // 지역

    @ApiModelProperty(value = "거래화폐단위")
    String currencyUnit;    // 거래화폐단위

    @ApiModelProperty(value = "회사담당자")
    String companyCharge;  // 회사담당자

    @ApiModelProperty(value = "회사담당부서")
    String companyChargeDept;  // 회사담당부서

    @ApiModelProperty(value = "대금결제방법")
    String paymentMethod;   // 대금결제방법

    @ApiModelProperty(value = "결제일자")
    String paymentDate;     // 결제일자

    @ApiModelProperty(value = "운송방법")
    String transitMethod;   // 운송방법

    @ApiModelProperty(value = "검사방법")
    String testMethod;      // 검사방법

    @ApiModelProperty(value = "휴대폰번호")
    String phoneNumber;     // 휴대폰번호

    @ApiModelProperty(value = "검색어")
    String searchWord;      // 검색어

    @ApiModelProperty(value = "사용여부")
    boolean useYn;  // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        @ApiModelProperty(value = "고유아이디")
        Long id;
        @ApiModelProperty(value = "거래처명")
        String name;
    }
}
