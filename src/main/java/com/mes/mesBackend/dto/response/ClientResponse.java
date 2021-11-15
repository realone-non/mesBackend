package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "거래처")
public class ClientResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "거래처코드")
    String clientCode;

    @Schema(description = "거래처명")
    String name;    // 거래처명

    @Schema(description = "약어")
    String shortName;      // 약어

    @Schema(description = "거래처 유형")
    ClientTypeResponse clientType;   // 거래처 유형

    @Schema(description = "사업자등록번호")
    String businessNumber;  // 사업자등록번호

    @Schema(description = "사업자등록증")
    String businessFile;    // 사업자등록증

    @Schema(description = "대표자명")
    String ceoName;     // 대표자명

    @Schema(description = "우편번호")
    String postalCode;      // 우편번호

    @Schema(description = "주소")
    String address;     // 주소

    @Schema(description = "상세주소")
    String detailAddress;   // 상세주소

    @Schema(description = "업태")
    BusinessTypeResponse businessType;      // 업태

    @Schema(description = "업종")
    String item;        // 업종

    @Schema(description = "전화번호")
    String telNumber;   // 전화번호

    @Schema(description = "fax번호")
    String faxNumber;   // fax번호 12자

    @Schema(description = "홈페이지")
    String homePageUrl; //  홈페이지 주소

    @Schema(description = "메일")
    String mail;        // 메일

    @Schema(description = "담당자 이름")
    String clientChargeName;  // 담당자 이름

    @Schema(description = "무역업등록번호")
    String tradeBusinessRegNo;  // 무역업등록번호

    @Schema(description = "통관고유번호")
    String pccc;            // 통관고유번호

    @Schema(description = "국가코드")
    CountryCodeResponse countryCode;     // 국가코드

    @Schema(description = "지역")
    String area;        // 지역

    @Schema(description = "거래화폐단위")
    String currencyUnit;    // 거래화폐단위

    @Schema(description = "회사담당자")
    String companyCharge;  // 회사담당자

    @Schema(description = "회사담당부서")
    String companyChargeDept;  // 회사담당부서

    @Schema(description = "대금결제방법")
    String paymentMethod;   // 대금결제방법

    @Schema(description = "결제일자")
    String paymentDate;     // 결제일자

    @Schema(description = "운송방법")
    String transitMethod;   // 운송방법

    @Schema(description = "검사방법")
    String testMethod;      // 검사방법

    @Schema(description = "휴대폰번호")
    String phoneNumber;     // 휴대폰번호

    @Schema(description = "검색어")
    String searchWord;      // 검색어

    @Schema(description = "사용여부")
    boolean useYn;  // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        @Schema(description = "고유아이디")
        Long id;
        @Schema(description = "거래처명")
        String name;
    }
}
