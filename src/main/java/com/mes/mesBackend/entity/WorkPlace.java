package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// 사업장
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_PLACES")
@Data
public class WorkPlace extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '사업장 고유아이디'")
    private Long id;

    @Column(name = "WORK_PLACE_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '사업장 코드'")
    private String workPlaceCode;   // 사업장 코드

    @Column(name = "WORK_PLACE_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '사업장명'")
    private String workPlaceName;   // 사업장명

    @Column(name = "BUSINESS_REG_NO", nullable = false, columnDefinition = "varchar(255) COMMENT '사업자등록번호'")
    private String businessRegNo;  // 사업자등록번호

    @Column(name = "CEO_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '대표자명'")
    private String ceoName;     // 대표자명

    @Column(name = "POSTAL_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '우편번호'")
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS", nullable = false, columnDefinition = "varchar(255) COMMENT '주소'")
    private String address;    // 주소

    @Column(name = "DETAIL_ADDRESS", columnDefinition = "varchar(255) COMMENT '상세주소'")
    private String detailAddress;    // 상세주소

    @Column(name = "ENG_ADDRESS_1", columnDefinition = "varchar(255) COMMENT '영문주소1'")
    private String engAddress1;     // 영문주소1

    @Column(name = "ENG_ADDRESS_2", columnDefinition = "varchar(255) COMMENT '영문주소2'")
    private String engAddress2;    // 영문주소2

    @OneToMany(mappedBy = "workPlace", fetch = FetchType.LAZY)
    private List<WorkPlaceBusinessType> type;   // 업태

    @Column(name = "ITEM", columnDefinition = "varchar(255) COMMENT '업종'")
    private String item;    // 업종

    @Column(name = "TEL_NUMBER", nullable = false, columnDefinition = "varchar(255) COMMENT '전화번호'")
    private String telNumber;  // 전화번호 11자

    @Column(name = "FAX_NUMBER", columnDefinition = "varchar(255) COMMENT 'fax 번호'")
    private String faxNumber;   // fax번호 12자

    @Column(name = "CORPORATE_CODE", columnDefinition = "varchar(255) COMMENT '법인코드'")
    private String corporateCode;   // 법인코드

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void put(WorkPlace newWorkPlace) {
        setWorkPlaceCode(newWorkPlace.workPlaceCode);
        setWorkPlaceName(newWorkPlace.workPlaceName);
        setBusinessRegNo(newWorkPlace.businessRegNo);
        setCeoName(newWorkPlace.ceoName);
        setPostalCode(newWorkPlace.postalCode);
        setAddress(newWorkPlace.address);
        setDetailAddress(newWorkPlace.detailAddress);
        setEngAddress1(newWorkPlace.engAddress1);
        setEngAddress2(newWorkPlace.engAddress2);
        setItem(newWorkPlace.item);
        setTelNumber(newWorkPlace.telNumber);
        setFaxNumber(newWorkPlace.faxNumber);
        setCorporateCode(newWorkPlace.corporateCode);
        setUseYn(newWorkPlace.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
