package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
public class ClientRequest {
    // 거래처코드
    @NotBlank(message = NOT_NULL)
    String clientCode;
    // 거래처명
    @NotBlank(message = NOT_NULL)
    String name;
    // 약어
    String shortName;
    // 거래처 유형
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long clientType;
    // 사업자등록번호
    @NotBlank(message = NOT_NULL)
    String businessNumber;
    String businessFile;   // 사업자등록증
    @NotBlank(message = NOT_NULL)
    String ceoName;    // 대표자명
    String postalCode;      // 우편번호
    String address;     // 주소
    String detailAddress;   // 상세주소
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long businessType;      // 업태 아이디
    String item;        // 업종
    String telNumber;   // 전화번호
    String faxNumber;   // fax번호 12자
    String homePageUrl; //  홈페이지 주소
    String mail;        // 메일
    String clientChargeName;  // 거래처 담당자 명
    String tradeBusinessRegNo;  // 무역업등록번호
    String pccc;            // 통관고유번호
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long countryCode;     // 국가코드 Id
    String area;        // 지역
    String currencyUnit;    // 거래화폐단위
    String companyCharge;  // 회사담당자
    String companyChargeDept;  // 회사담당부서
    String paymentMethod;   // 대금결제방법
    String paymentDate;     // 결제일자
    String transitMethod;   // 운송방법
    String testMethod;      // 검사방법
    String searchWord;      // 검색어
    String phoneNumber;     // 휴대폰번호
    @NotNull(message = NOT_NULL)
    boolean useYn;          // 사용여부
}
