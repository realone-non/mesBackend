package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {
    Long id;
    String clientCode;  // 거래처코드
    String name;    // 거래처명
    String shortName;      // 약어
    ClientTypeResponse clientType;   // 거래처 유형
    String businessNumber;  // 사업자등록번호
    String businessFile;    // 사업자등록증
    String ceoName;     // 대표자명
    String postalCode;      // 우편번호
    String address;     // 주소
    String detailAddress;   // 상세주소
    BusinessTypeResponse businessType;      // 업태
    String item;        // 업종
    String telNumber;   // 전화번호
    String faxNumber;   // fax번호 12자
    String homePageUrl; //  홈페이지 주소
    String mail;        // 메일
    String clientChargeName;  // 담당자 이름
    String tradeBusinessRegNo;  // 무역업등록번호
    String pccc;            // 통관고유번호
    CountryCodeResponse countryCode;     // 국가코드
    String area;        // 지역
    String currencyUnit;    // 거래화폐단위
    String companyCharge;  // 회사담당자
    String companyChargeDept;  // 회사담당부서
    String paymentMethod;   // 대금결제방법
    String paymentDate;     // 결제일자
    String transitMethod;   // 운송방법
    String testMethod;      // 검사방법
    String phoneNumber;     // 휴대폰번호
    String searchWord;      // 검색어
    boolean useYn;  // 사용여부
}