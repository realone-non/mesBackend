package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;

// 거래처
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "CLIENTS")
@Data
public class Client extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "CLIENT_CODE", nullable = false)
    private String clientCode;  // 거래처코드

    @Column(name = "NAME", nullable = false)
    private String name;    // 거래처명

    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;      // 약어

    @ManyToOne @JoinColumn(name = "CLIENT_TYPES_ID", nullable = false)
    private ClientType clientType;   // 거래처 유형

    @Column(name = "BUSINESS_REG_NO", nullable = false, length = 10)
    private String businessNumber;  // 사업자등록번호

    @Column(name = "BUSINESS_FILE")
    private String businessFile;    // 사업자등록증

    @Column(name = "CEO_NAME", nullable = false)
    private String ceoName;     // 대표자명

    @Column(name = "POSTAL_CODE", nullable = false, length = 6)
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS", nullable = false)
    private String address;     // 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;   // 상세주소

    @ManyToOne @JoinColumn(name = "BUSINESS_TYPES_ID", nullable = false)
    private BusinessType businessType;      // 업태

    @Column(name = "ITEM")
    private String item;        // 업종

    @Column(name = "TEL_NUMBER", nullable = false, length = 11)
    private String telNumber;   // 전화번호

    @Column(name = "FAX_NUMBER", length = 12)
    private String faxNumber;   // fax번호 12자

    @Column(name = "HOME_PAGE_URL")
    private String homePageUrl; //  홈페이지 주소

    @Column(name = "MAIL", nullable = false)
    private String mail;        // 메일

    @Column(name = "CLIENT_CHARGER_NAME")
    private String clientChargeName;  // 담당자 이름

    @Column(name = "TRADE_BUSINESS_REG_NO")
    private String tradeBusinessRegNo;  // 무역업등록번호

    @Column(name = "PCCC")
    private String pccc;            // 통관고유번호

    @ManyToOne @JoinColumn(name = "COUNTRY_CODE_ID")
    private CountryCode countryCode;     // 국가코드

    @Column(name = "AREA")
    private String area;        // 지역

    @Column(name = "CURRENCY_UNIT")
    private String currencyUnit;    // 거래화폐단위

    @Column(name = "COMPANY_CHARGE")
    private String companyCharge;  // 회사담당자

    @Column(name = "COMPANY_CHARGE_DEPT")
    private String companyChargeDept;  // 회사담당부서

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;   // 대금결제방법

    @Column(name = "PAYMENT_DATE")
    private String paymentDate;     // 결제일자

    @Column(name = "TRANSIT_METHOD")
    private String transitMethod;   // 운송방법

    @Column(name = "TEST_METHOD")
    private String testMethod;      // 검사방법

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;     // 휴대폰번호

    @Column(name = "SEARCH_WORD")
    private String searchWord;      // 검색어

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "USE_YN")
    private boolean useYn = true;       // 사용여부

    // 수정 매핑
    public void put(Client newClient) {
        setClientCode(newClient.clientCode);
        setName(newClient.name);
        setShortName(newClient.shortName);
        setClientType(newClient.clientType);
        setBusinessNumber(newClient.businessNumber);
        setBusinessFile(newClient.businessFile);
        setCeoName(newClient.ceoName);
        setPostalCode(newClient.postalCode);
        setAddress(newClient.address);
        setDetailAddress(newClient.detailAddress);
        setBusinessType(newClient.businessType);
        setItem(newClient.item);
        setTelNumber(newClient.telNumber);
        setFaxNumber(newClient.faxNumber);
        setHomePageUrl(newClient.homePageUrl);
        setMail(newClient.mail);
        setClientChargeName(newClient.clientChargeName);
        setTradeBusinessRegNo(newClient.tradeBusinessRegNo);
        setPccc(newClient.pccc);
        setCountryCode(newClient.countryCode);
        setArea(newClient.area);
        setCurrencyUnit(newClient.currencyUnit);
        setCompanyCharge(newClient.companyCharge);
        setCompanyChargeDept(newClient.companyChargeDept);
        setPaymentMethod(newClient.paymentMethod);
        setPaymentDate(newClient.paymentDate);
        setTransitMethod(newClient.transitMethod);
        setTestMethod(newClient.testMethod);
        setSearchWord(newClient.searchWord);
        setUseYn(newClient.useYn);
    }

    public void putJoinTable(
            BusinessType businessType,
            CountryCode countryCode,
            ClientType clientType
    ) {
        setBusinessType(businessType);
        setCountryCode(countryCode);
        setClientType(clientType);
    }
}