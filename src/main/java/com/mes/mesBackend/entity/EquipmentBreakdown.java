package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 17-2. 설비 고장 수리 내역
 * 검색: 공장,작업장,설비유형,작업기간
 * 고장발생일
 * 설비코드              -> Equipment
 * 설비명               -> Equipment
 * 설비유형              -> Equipment
 * '신고일시
 * 요청 SMS 유형
 * 요청시 고장유형
 * 요청비고
 * 고장유형
 * 고자원인
 * 보전원 도착일시
 * 수리시작일시
 * 수리종료일시
 * 비고
 * 수리전 이미지 파일
 * 수리후 이미지 파일
 * 작업장
 * 17-4. 추가 컬럼: 수리부품,수리부품명
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "EQUIPMENT_BREAKDOWNS")
@Data
public class EquipmentBreakdown extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비고장 수리정보 고유아이디'")
    private Long id;

    @Column(name = "BREAKDOWN_DATE", columnDefinition = "date COMMENT '고장발생일'")
    private LocalDate breakDownDate;        // 고장발생일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPMENT", columnDefinition = "bigint COMMENT '설비정보'")
    private Equipment equipment;            // 설비정보

    @Column(name = "REPORT_DATE", columnDefinition = "date COMMENT '신고일시'")
    private LocalDate reportDate;       // 신고일시

    @Column(name = "REQUEST_SMS_TYPE", columnDefinition = "varchar(255) COMMENT '요청 SMS 유형'")
    private String requestSmsType;          // 요청 SMS 유형

    @Column(name = "REQUEST_BREAK_TYPE", columnDefinition = "varchar(255) COMMENT '요청 고장유형'")
    private String requestBreakType;            // 요청 고장유형

    @Column(name = "BREAK_NOTE", columnDefinition = "varchar(255) COMMENT '요청비고'")
    private String breakNote;       // 요청비고

    @Column(name = "BREAK_REASON", columnDefinition = "varchar(255) COMMENT '고장이유'")
    private String breakReason;     // 고장이유

    @Column(name = "ARRIVAL_DATE", columnDefinition = "date COMMENT '보전원 도착일시'")
    private LocalDate arrivalDate;      // 보전원 도착일시

    @Column(name = "REPAIR_START_DATE", columnDefinition = "date COMMENT '수리시작일시'")
    private LocalDate repairStartDate;      // 수리 시작일시

    @Column(name = "REPAIR_END_DATE", columnDefinition = "date COMMENT '수리종료일시'")
    private LocalDate repairEndDate;        // 수리 종료일시

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    @Column(name = "BEFORE_REPAIR_FILE_URL", columnDefinition = "varchar(255) COMMENT '수리전 이미지 파일'")
    private String beforeRepairFileUrl;     // 수리전 이미지 파일

    @Column(name = "AFTER_REPAIR_FILE_URL", columnDefinition = "varchar(255) COMMENT '수리후 이미지 파일'")
    private String afterRepairFileUrl;      // 수리후 이미지 파일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_CENTER", columnDefinition = "bigint COMMENT '작업장'")
    private WorkCenter workCenter;          // 작업장
}
