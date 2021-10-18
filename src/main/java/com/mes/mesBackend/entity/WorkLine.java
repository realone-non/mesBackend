package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 작업라인 등록
 * 검색조건 : 공장
 * 라인코드(01),
 * 작업라인명(조립라인,몰딩라인,외주라인),
 * 작업장(1작업장(본사),외주작업장(리버텍)) -> 작업장 참조(WORK_CENTERS),
 * 원자재창고(kitting창고(본사)) -> WARE_HOUSE 참조 / 일단 빼고 작업,
 * 작업공정(조립,몰딩,조립) -> WORK_PROCESS,
 * pop시작 FORMID(POP1003),
 * 외주사(리버텍,신성테크) -> 작업장 참조(WORK_CENTERS) / 일단 빼고 작업,
 * 일 가동시간(8,8), 사용
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_LINES")
@Data
public class WorkLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "WORK_LINE_CODE", nullable = false, unique = true)
    private String workLineCode;   // 라인코드

    @Column(name = "WORK_LINE_NAME", nullable = false)
    private String workLineName;    // 작업라인명

    @OneToOne @JoinColumn(name = "WORK_CENTER")
    private WorkCenter workCenter;  // 작업장

    @OneToOne @JoinColumn(name = "WARE_PROCESS", nullable = false)
    private WorkProcess workProcess;        // 작업공정

    @Column(name = "POP_START_FORMID", nullable = false)
    private String popStartFormid;      // POP 시작 FORMID

    @Column(name = "TIME", nullable = false)
    private int time;       // 일 가동시간

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne @JoinColumn(name = "FACTORY")
    private Factory factory;                // 공장
}
