package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 6-2. 작업지시 정보
 * 지시번호 (2107080001-0004)
 * 작업공정 (조립)                        -> WorkProcess
 * 작업라인 (외주라인(리버텍))               -> WorkLine
 * 지시수량 (20)
 * 생산담당자 ()                          -> Manager
 * 단위 (개)                             -> Unit
 * 준비시간(분) (0)
 * UPH (1)
 * 소요시간(분) (1200)
 * 작업예정일 (2021.7.19)
 * 예정시간 (09:00)
 * 지시상태 (완료)
 * 검사의뢰 (자동검사)                      -> TestType
 * 검사유형 (출하검사)                      -> TestProcess
 * 최종공정 (아니오)
 * 생산수량 (아니오)
 * 투입인원 (1)
 * 비고 ()
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_ORDER_DETAILS")
@Data
public class WorkOrderDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 정보 고유아이디'")
    private Long id;

    @Column(name = "ORDER_NO", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '지시번호'")
    private String orderNo;             // 지시번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS", nullable = false, columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;        // 작업공정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_LINE", nullable = false, columnDefinition = "bigint COMMENT '작업라인'")
    private WorkLine workLine;              // 작업라인

    @Column(name = "ORDER_AMOUNT", nullable = false, columnDefinition = "int COMMENT '지시수량'")
    private int orderAmount;                // 지시수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER", columnDefinition = "bigint COMMENT '생산담당자'")
    private Manager manager;                // 생산담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", nullable = false, columnDefinition = "bigint COMMENT '단위'")
    private Unit unit;                      // 단위

    @Column(name = "READY_TIME", columnDefinition = "bigint COMMENT '준비시간'")
    private Long readyTime;                 // 준비시간

    @Column(name = "UHP", columnDefinition = "int COMMENT 'UPH'")
    private int uph;                        // UPH

    @Column(name = "COST_TIME", nullable = false, columnDefinition = "bigint COMMENT '소요시간'")
    private Long costTime;                  // 소요시간

    @Column(name = "EXPECTED_DATE", nullable = false, columnDefinition = "date COMMENT '작업예정일'")
    private LocalDate expectedDate;         // 작업예정일

    @Column(name = "EXPECTED_TIME", nullable = false, columnDefinition = "bigint COMMENT '예정시간'")
    private Long expectedTime;              // 예정시간

    @Column(name = "ORDER_STATE",nullable = false, columnDefinition = "varchar(255) COMMENT '지시상태'")
    private String orderState;              // 지시상태

    // emun
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT '검사의뢰'")
    private TestType testType = TestType.NO_TEST;              // 검사의뢰

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_PROCESS", columnDefinition = "bigint COMMENT '검사유형'")
    private TestProcess testProcess;        // 검사유형

    @Column(name = "LAST_PROCESS_YN", nullable = false, columnDefinition = "bit(1) COMMENT '최종공정'")
    private boolean lastProcessYn;          // 최종공정

    @Column(name = "PRODUCTION_AMOUNT", columnDefinition = "int COMMENT '생산수량'")
    private int productionAmount;           // 생산수량

    @Column(name = "INPUT_PEOPLE", columnDefinition = "int COMMENT '투입인원'")
    private int inputPeople;                // 투입인원

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                    // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER", columnDefinition = "bigint COMMENT '작업지시 제조오더'")
    private WorkOrder workOrder;            // 작업지시 제조오더
}
