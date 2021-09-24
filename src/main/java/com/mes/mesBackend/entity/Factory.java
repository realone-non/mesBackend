package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "FACTORIES")
@Data
public class Factory extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "FACTORY_CODE")
    private String factoryCode;     // 공장코드

    @Column(name = "FACTORY_NAME")
    private String factoryName;     // 공장명

    @Column(name = "SHORT_NAME", length = 3)
    private String shortName;       // 약어

    @Column(name = "POSTAL_CODE", length = 6)
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS")
    private String address;         // 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;   // 상세주소

    @Column(name = "ENG_ADDRESS_1")
    private String engAddress1;     // 영문주소1

    @Column(name = "ENG_ADDRESS_2")
    private String engAddress2;     // 영문주소2

    @Column(name = "TEL_NUMBER", length = 11)
    private String telNumber;       // 전화번호

    @Column(name = "FAX_NUMBER", length = 12)
    private String faxNumber;       // fax번호

    @Column(name = "LOT_CODE", length = 1)
    private String lotCode;         // lot용 코드

    @ManyToOne @JoinColumn(name = "WORK_PLACES_ID")
    private WorkPlace workPlace;    // 사업장

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부
}
