package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 공장등록
* 공장코드 (p10,p20)
* 공장명 (본사,연구소)
* 약어 (E)
* 우편번호
* 상세주소
* 영문주소
* 영문주소2
* 전화번호
* fax
* LOT용 코드
* 사업장
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "FACTORIES")
@Data
public class Factory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "FACTORY_CODE")
    private String factoryCode;     // 공장코드

    @Column(name = "FACTORY_NAME")
    private String factoryName;     // 공장명

    @Column(name = "SHORT_NAME")
    private String shortName;       // 약어

    @Column(name = "POSTAL_CODE")
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS")
    private String address;         // 기본주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;   // 상세주소

    @Column(name = "ENG_ADDRESS_1")
    private String engAddress1;     // 영문주소1

    @Column(name = "ENG_ADDRESS_2")
    private String engAddress2;     // 영문주소2

    @Column(name = "TEL_NUMBER")
    private String telNumber;       // 전화번호

    @Column(name = "FAX_NUMBER")
    private String faxNumber;       // fax번호

    @Column(name = "LOT_CODE")
    private String lotCode;         // LOT용 코드

    // 1:1 단방향 매핑
    @OneToOne @JoinColumn(name = "WORK_PLACES_ID")
    private WorkPlace workPlace;    // 사업장

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    public void put(Factory newFactory, WorkPlace newWorkPlace) {
        setFactoryCode(newFactory.factoryCode);
        setFactoryName(newFactory.factoryName);
        setShortName(newFactory.shortName);
        setPostalCode(newFactory.postalCode);
        setAddress(newFactory.address);
        setDetailAddress(newFactory.detailAddress);
        setEngAddress1(newFactory.engAddress1);
        setEngAddress2(newFactory.engAddress2);
        setTelNumber(newFactory.telNumber);
        setFaxNumber(newFactory.faxNumber);
        setLotCode(newFactory.lotCode);
        setWorkPlace(newWorkPlace);
    }
}
