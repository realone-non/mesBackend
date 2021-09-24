package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 사업장
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_PLACES")
@Data
public class WorkPlace extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "WORK_PLACE_CODE", nullable = false)
    private String workPlaceCode;   // 사업장 코드

    @Column(name = "WORK_PLACE_NAME", nullable = false)
    private String workPlaceName;   // 사업장명

    @Column(name = "BUSINESS_REG_NO", nullable = false, length = 10)
    private String businessRegNo;  // 사업자등록번호

    @Column(name = "CEO_NAME", nullable = false, length = 5)
    private String ceoName;     // 대표자명

    @Column(name = "POSTAL_CODE", nullable = false, length = 6)
    private String postalCode;      // 우편번호

    @Column(name = "ADDRESS", nullable = false)
    private String address;    // 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;    // 상세주소

    @Column(name = "ENG_ADDRESS_1")
    private String engAddress1;     // 영문주소1

    @Column(name = "ENG_ADDRESS_2")
    private String engAddress2;    // 영문주소2

    @ManyToOne @JoinColumn(name = "BUSINESS_TYPES_ID", nullable = false)
    private BusinessType type;    // 업태

    @Column(name = "ITEM")
    private String item;    // 업종

    @Column(name = "TEL_NUMBER", nullable = false, length = 11)
    private String telNumber;  // 전화번호 11자

    @Column(name = "FAX_NUMBER", length = 12)
    private String faxNumber;   // fax번호 12자

    @Column(name = "CORPORATE_CODE")
    private String corporateCode;   // 법인코드

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;      // 사용여부
}
