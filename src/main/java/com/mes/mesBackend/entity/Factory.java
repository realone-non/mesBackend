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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '공장등록 고유아이디'")
    private Long id;

    @Column(name = "FACTORY_CODE", columnDefinition = "varchar(255) COMMENT '공장코드'", nullable = false)
    private String factoryCode;     // 공장코드

    @Column(name = "FACTORY_NAME", columnDefinition = "varchar(255) COMMENT '공장명'", nullable = false)
    private String factoryName;     // 공장명

    @Column(name = "SHORT_NAME", columnDefinition = "varchar(255) COMMENT '약어'")
    private String shortName;       // 약어

    @Column(name = "POSTAL_CODE", columnDefinition = "varchar(255) COMMENT '우편번호'")
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS", columnDefinition = "varchar(255) COMMENT '기본주소'")
    private String address;         // 기본주소

    @Column(name = "DETAIL_ADDRESS", columnDefinition = "varchar(255) COMMENT '상세주소'")
    private String detailAddress;   // 상세주소

    @Column(name = "ENG_ADDRESS_1", columnDefinition = "varchar(255) COMMENT '영문주소1'")
    private String engAddress1;     // 영문주소1

    @Column(name = "ENG_ADDRESS_2", columnDefinition = "varchar(255) COMMENT '영문주소2'")
    private String engAddress2;     // 영문주소2

    @Column(name = "TEL_NUMBER", columnDefinition = "varchar(255) COMMENT '전화번호'")
    private String telNumber;       // 전화번호

    @Column(name = "FAX_NUMBER", columnDefinition = "varchar(255) COMMENT 'fax번호'")
    private String faxNumber;       // fax번호

    @Column(name = "LOT_CODE", columnDefinition = "varchar(255) COMMENT 'LOT용 코드'")
    private String lotCode;         // LOT용 코드

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PLACES_ID", columnDefinition = "bigint COMMENT '사업장'", nullable = false)
    private WorkPlace workPlace;    // 사업장

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
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

    public void addJoin(WorkPlace workPlace) {
        setWorkPlace(workPlace);
    }

    public void delete() {
        setDeleteYn(true);
    }

}
