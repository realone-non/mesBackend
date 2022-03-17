package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.InspectionType;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

/*
 * 거래처등록
 * 검색: 거래처유형,거래처코드,거래처명
 * 거래처코드 (1010633960)
 * 거래처명 (백승종합상사)
 * 약어명 ()
 * 거래처유형 (매입매출처)
 * 사업자등록번호 (1010607308)
 * 대표자명 (박상균)
 * 우편번호 (20148)
 * 주소 (서울특별시 관악구 신림동 1455-26)
 * 상세주소 ()
 * 업태 (도소매)
 * 업종 (전자부품)
 * 전화번호 (02-2277-6102)
 * FAX 번호 (031-733-1090)
 * 홈페이지 주소
 * EMAIL ()
 * 거래처담당자명 (김선아 과장님)
 * 무역업등록번호 ()
 * 통관고유번호 ()
 * 국가코드 ()
 * 지역 ()
 * 거래화폐단위 ()
 * 회사담당자 (오석진)
 * 회사담당부서 ()
 * 대금결제방법
 * 결제일자
 * 운송방법
 * 검사방법
 * 사용
 * 휴대폰번호
 * 검색어 (홍콩대리점)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "CLIENTS")
@Data
public class Client extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '거래처 고유아이디'")
    private Long id;

    @Column(name = "CLIENT_CODE", columnDefinition = "varchar(255) COMMENT '거래처 코드'")
    private String clientCode;  // 거래처코드

    @Column(name = "CLIENT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '거래처 명'")
    private String clientName;    // 거래처명

    @Column(name = "SHORT_NAME", columnDefinition = "varchar(255) COMMENT '약어'")
    private String shortName;      // 약어

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT_TYPE", nullable = false, columnDefinition = "bigint COMMENT '거래처 유형'")
    private ClientType clientType;   // 거래처 유형

    @Column(name = "BUSINESS_REG_NO", nullable = false, columnDefinition = "varchar(255) COMMENT '사업자등록번호'")
    private String businessNumber;  // 사업자등록번호

    @Column(name = "BUSINESS_FILE", columnDefinition = "varchar(255) COMMENT '사업자등록증'")
    private String businessFile;    // 사업자등록증

    @Column(name = "CEO_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '대표자명'")
    private String ceoName;     // 대표자명

    @Column(name = "POSTAL_CODE", columnDefinition = "varchar(255) COMMENT '우편번호'")
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS", columnDefinition = "varchar(255) COMMENT '주소'")
    private String address;     // 주소

    @Column(name = "DETAIL_ADDRESS", columnDefinition = "varchar(255) COMMENT '상세주소'")
    private String detailAddress;   // 상세주소

    @Column(name = "BUSINESS_TYPE", columnDefinition = "varchar(255) COMMENT '업태'")
    private String businessType;      // 업태

    @Column(name = "ITEM", columnDefinition = "varchar(255) COMMENT '업종'")
    private String item;        // 업종

    @Column(name = "TEL_NUMBER", columnDefinition = "varchar(255) COMMENT '전화번호'")
    private String telNumber;   // 전화번호

    @Column(name = "FAX_NUMBER", columnDefinition = "varchar(255) COMMENT 'fax번호'")
    private String faxNumber;   // fax번호 12자

    @Column(name = "HOME_PAGE_URL", columnDefinition = "varchar(255) COMMENT '홈페이지 주소'")
    private String homePageUrl; //  홈페이지 주소

    @Column(name = "MAIL",columnDefinition = "varchar(255) COMMENT '메일'")
    private String mail;        // 메일

    @Column(name = "CLIENT_CHARGER_NAME", columnDefinition = "varchar(255) COMMENT '거래처담당자명'")
    private String clientChargeName;  // 담당자 이름

    @Column(name = "TRADE_BUSINESS_REG_NO", columnDefinition = "varchar(255) COMMENT '무역업등록번호'")
    private String tradeBusinessRegNo;  // 무역업등록번호

    @Column(name = "PCCC", columnDefinition = "varchar(255) COMMENT '통관고유번호'")
    private String pccc;            // 통관고유번호

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "COUNTRY_CODE", columnDefinition = "bigint COMMENT '국가코드'")
    private CountryCode countryCode;     // 국가코드

    @Column(name = "AREA", columnDefinition = "varchar(255) COMMENT '지역'")
    private String area;        // 지역

    @ManyToOne(fetch =LAZY)
    @JoinColumn(name = "CURRENCY_UNIT", columnDefinition = "bigint(1) COMMENT '거래화폐단위'")
    private Currency currencyUnit;    // 거래화폐단위

    @Column(name = "COMPANY_CHARGE", columnDefinition = "varchar(255) COMMENT '회사담당자'")
    private String companyCharge;  // 회사담당자

    @Column(name = "COMPANY_CHARGE_DEPT", columnDefinition = "varchar(255) COMMENT '회사담당부서'")
    private String companyChargeDept;  // 회사담당부서

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PAYMENT_METHOD", columnDefinition = "bigint(1) COMMENT '대금결제방법'")
    private PayType paymentMethod;   // 대금결제방법

    @Column(name = "PAYMENT_DATE", columnDefinition = "varchar(255) COMMENT '결제일자'")
    private String paymentDate;     // 결제일자

    @Column(name = "TRANSIT_METHOD", columnDefinition = "varchar(255) COMMENT '운송방법'")
    private String transitMethod;   // 운송방법

    @Enumerated(STRING)
    @Column(name = "INSPECTION_TYPE", columnDefinition = "varchar(255) COMMENT '검사방법'")
    private InspectionType inspectionType;      // 검사방법

    @Column(name = "PHONE_NUMBER", columnDefinition = "varchar(255) COMMENT '휴대폰번호'")
    private String phoneNumber;     // 휴대폰번호

    @Column(name = "SEARCH_WORD", columnDefinition = "varchar(255) COMMENT '검색어'")
    private String searchWord;      // 검색어

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;       // 사용여부

    // 수정 매핑
    public void put(
            Client newClient,
            CountryCode newCountryCode,
            ClientType newClientType,
            Currency newCurrency,
            PayType newPayType
    ) {
        setClientCode(newClient.clientCode);
        setClientName(newClient.clientName);
        setShortName(newClient.shortName);
        setClientType(newClientType);
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
        setCountryCode(newCountryCode);
        setArea(newClient.area);
        setCurrencyUnit(newCurrency);
        setCompanyCharge(newClient.companyCharge);
        setCompanyChargeDept(newClient.companyChargeDept);
        setPaymentMethod(newPayType);
        setPaymentDate(newClient.paymentDate);
        setTransitMethod(newClient.transitMethod);
        setInspectionType(inspectionType);
        setSearchWord(newClient.searchWord);
        setUseYn(newClient.useYn);
    }

    public void addJoin(
            CountryCode countryCode,
            ClientType clientType,
            Currency currency,
            PayType payType
    ) {
        setCountryCode(countryCode);
        setClientType(clientType);
        setCurrencyUnit(currency);
        setPaymentMethod(payType);
    }

    public void delete() {
        setDeleteYn(true);
    }
}