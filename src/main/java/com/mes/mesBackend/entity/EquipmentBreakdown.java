package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.BreakReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

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
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "EQUIPMENT_BREAKDOWNS")
@Data
public class EquipmentBreakdown extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비고장 수리정보 고유아이디'")
    private Long id;

    @Column(name = "BREAKDOWN_DATE", columnDefinition = "date COMMENT '고장발생일'", nullable = false)
    private LocalDate breakDownDate;        // 고장발생일

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT", columnDefinition = "bigint COMMENT '설비정보'", nullable = false)
    private Equipment equipment;            // 설비정보

    @Column(name = "REPORT_DATE", columnDefinition = "datetime COMMENT '신고일시'", nullable = false)
    private LocalDateTime reportDate;       // 신고일시

    @Column(name = "REQUEST_BREAK_TYPE", columnDefinition = "varchar(255) COMMENT '요청시 고장유형'", nullable = false)
    private String requestBreakType;            // 요청시 고장유형

    @Column(name = "BREAK_NOTE", columnDefinition = "varchar(255) COMMENT '요청비고'")
    private String breakNote;       // 요청비고

    @Enumerated(STRING)
    @Column(name = "BREAK_REASON", columnDefinition = "varchar(255) COMMENT '고장유형'", nullable = false)
    private BreakReason breakReason;     // 고장유형

    @Column(name = "CAUSE_OF_FAILURE", columnDefinition = "varchar(255) COMMENT '고장원인'", nullable = false)
    private String causeOfFailure;  // 고장원인

    @Column(name = "ARRIVAL_DATE", columnDefinition = "datetime COMMENT '보전원 도착일시'")
    private LocalDateTime arrivalDate;      // 보전원 도착일시

    @Column(name = "REPAIR_START_DATE", columnDefinition = "datetime COMMENT '수리시작일시'")
    private LocalDateTime repairStartDate;      // 수리 시작일시

    @Column(name = "REPAIR_END_DATE", columnDefinition = "datetime COMMENT '수리종료일시'")
    private LocalDateTime repairEndDate;        // 수리 종료일시

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_CENTER", columnDefinition = "bigint COMMENT '작업장'", nullable = false)
    private WorkCenter workCenter;          // 작업장

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void add(Equipment equipment, WorkCenter workCenter) {
        setEquipment(equipment);
        setWorkCenter(workCenter);
    }

    public void update(EquipmentBreakdown newEquipmentBreakdown, Equipment newEquipment, WorkCenter newWorkCenter) {
        setBreakDownDate(newEquipmentBreakdown.breakDownDate);
        setEquipment(newEquipment);
        setReportDate(newEquipmentBreakdown.reportDate);
        setRequestBreakType(newEquipmentBreakdown.requestBreakType);
        setBreakNote(newEquipmentBreakdown.breakNote);
        setBreakReason(newEquipmentBreakdown.breakReason);
        setCauseOfFailure(newEquipmentBreakdown.causeOfFailure);
        setArrivalDate(newEquipmentBreakdown.arrivalDate);
        setRepairStartDate(newEquipmentBreakdown.repairStartDate);
        setRepairEndDate(newEquipmentBreakdown.repairEndDate);
        setNote(newEquipmentBreakdown.note);
        setWorkCenter(newWorkCenter);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
