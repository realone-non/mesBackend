package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 설비 등록
 * 검색: 공장, 설비명, 점검대상
 * 설비코드 (EM-5001)
 * 설비명 (진공함침기)
 * 설비유형
 * 규격&모델 (DVT-100)
 * 구매처 (대아)
 * 구매일자 (날짜)
 * 구입금액 (192000)
 * 생산업체명 (대아)
 * 시리얼번호 (DVT-100)
 * 생산개시일자 (날짜)
 * 작업장 (1작업장)
 * 작업공정 (몰딩)
 * 작업라인 (준비라인)
 * 점검주기(월) (30,90)
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "EQUIPMENTS")
@Data
public class Equipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @Column(name = "EQUIPMENT_CODE", nullable = false, unique = true)
    private String equipmentCode;        // 설비코드

    @Column(name = "EQUIPMENT_NAME", nullable = false, unique = true)
    private String equipmentName;        // 설비명

    @Column(name = "EQUIPMENT_TYPE")
    private String equipmentType;        // 설비유형

    @Column(name = "MODEL")
    private String model;               // 규격&모델

    @Column(name = "PURCHASE_COMPANY")
    private String purchaseCompany;     // 구매처

    @Column(name = "PURCHASE_DATE")
    private String purchaseDate;        // 구매일자

    @Column(name = "PURCHASE_AMOUNT")
    private String purchaseAmount;      // 구입금액

    @Column(name = "MAKER")
    private String maker;               // 생산업체명

    @Column(name = "SERIAL_NO")
    private String serialNo;            // 시리얼번호

    @Column(name = "START_DATE")
    private LocalDate startDate;        // 생산개시일자

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "WORK_CENTER_ID")
    private WorkCenter workCenter;        // 작업장

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "WORK_PROCESS_ID")
    private WorkProcess workProcess;        // 작업공정

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "WORK_LINE_ID")
    private WorkLine workLine;              // 작업라인

    @Column(name = "CHECK_CYCLE")
    private int checkCycle;             // 점검주기

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장
}
