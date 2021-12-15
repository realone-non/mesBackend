package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.querydsl.core.annotations.QueryInit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "WORK_ORDER_DETAILS")
@Data
public class WorkOrderDetail extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업지시 정보 고유아이디'")
    private Long id;

    @Column(name = "ORDER_NO", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '지시번호'")
    private String orderNo;             // 지시번호

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", nullable = false, columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;        // 작업공정

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_LINE", nullable = false, columnDefinition = "bigint COMMENT '작업라인'")
    private WorkLine workLine;              // 작업라인

    @Column(name = "ORDER_AMOUNT", nullable = false, columnDefinition = "int COMMENT '지시수량'")
    private int orderAmount;                // 지시수량

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '생산담당자'")
    private User user;                // 생산담당자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENT '단위'")
    private Unit unit;

    @Column(name = "READY_TIME", columnDefinition = "int COMMENT '준비시간'", nullable = false)
    private int readyTime;                 // 준비시간

    @Column(name = "UHP", columnDefinition = "int COMMENT 'UPH'", nullable = false)
    private int uph;                        // UPH

    @Column(name = "COST_TIME", nullable = false, columnDefinition = "int COMMENT '소요시간'")
    private int costTime;                  // 소요시간

    @Column(name = "EXPECTED_WORK_DATE", nullable = false, columnDefinition = "date COMMENT '작업예정일'")
    private LocalDate expectedWorkDate;         // 작업예정일

    @Column(name = "EXPECTED_WORK_TIME", nullable = false, columnDefinition = "varchar(255) COMMENT '예정시간'")
    private String expectedWorkTime;              // 예정시간

    @Enumerated(STRING)
    @Column(name = "ORDER_STATE",nullable = false, columnDefinition = "varchar(255) COMMENT '지시상태'")
    private OrderState orderState;              // 지시상태

    @Enumerated(STRING)
    @Column(name = "TEST_TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT '검사의뢰'")
    private TestType testType;              // 검사의뢰

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEST_PROCESS", columnDefinition = "bigint COMMENT '검사유형'", nullable = false)
    private TestProcess testProcess;        // 검사유형

    @Column(name = "LAST_PROCESS_YN", nullable = false, columnDefinition = "bit(1) COMMENT '최종공정'")
    private boolean lastProcessYn;          // 최종공정

    @Column(name = "PRODUCTION_AMOUNT", columnDefinition = "int COMMENT '생산수량'", nullable = false)
    private int productionAmount;           // 생산수량

    @Column(name = "INPUT_USER", columnDefinition = "int COMMENT '투입인원'", nullable = false)
    private int inputUser;                // 투입인원

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                    // 비고

    @QueryInit("*.*")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRODUCE_ORDER", columnDefinition = "bigint COMMENT '제조오더'")
    private ProduceOrder produceOrder;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '작업순번'")
    private int orders;

    public void add(
            WorkProcess workProcess,
            WorkLine workLine,
            User user,
            Unit unit,
            TestProcess testProcess,
            ProduceOrder produceOrder
    ) {
        setWorkProcess(workProcess);
        setWorkLine(workLine);
        setUser(user);
        setUnit(unit);
        setTestProcess(testProcess);
        setProduceOrder(produceOrder);
    }

    public void update(
            WorkOrderDetail newWorkOrderDetail,
            WorkProcess newWorkProcess,
            WorkLine newWorkLine,
            User newUser,
            TestProcess newTestProcess,
            Unit newUnit
    ) {
        setWorkProcess(newWorkProcess);
        setWorkLine(newWorkLine);
        setOrderAmount(newWorkOrderDetail.orderAmount);
        setUser(newUser);
        setUnit(newUnit);
        setReadyTime(newWorkOrderDetail.readyTime);
        setUph(newWorkOrderDetail.uph);
        setCostTime(newWorkOrderDetail.costTime);
        setExpectedWorkDate(newWorkOrderDetail.expectedWorkDate);
        setExpectedWorkTime(newWorkOrderDetail.expectedWorkTime);
        setOrderState(newWorkOrderDetail.orderState);
        setTestType(newWorkOrderDetail.testType);
        setTestProcess(newWorkOrderDetail.testProcess);
        setLastProcessYn(newWorkOrderDetail.lastProcessYn);
        setProductionAmount(newWorkOrderDetail.productionAmount);
        setInputUser(newWorkOrderDetail.inputUser);
        setNote(newWorkOrderDetail.note);
        setTestProcess(newTestProcess);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
