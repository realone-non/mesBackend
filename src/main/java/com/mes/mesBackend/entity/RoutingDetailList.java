package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


/* 라우팅 상세 등록
 * 작업순번 (01,02,03)
 * 작업공정 (조립,몰딩,포장)      -> WorkProcess
 * 작업장 (외주작업장,1작업장)     -> WorkCenter
 * 검사유형 (공정검사,출하검사)     -> TestCategory
 * 원자재창고 (kitting창고)      -> WareHouse
 * 입고창고 (리버텍_PCB Assy 공정,본사 생산조립 공정)    -> WareHouse
 * 메인공정 (예,아니오)
 * 최종공정 (예,아니오)
 * 작업개시일 (당일)
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ROUTING_DETAILS_LISTS")
@Data
public class RoutingDetailList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '라우팅 상세등록 고유아이디'")
    private Long id;

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '작업순번'")
    private int orders;     // 작업순번

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;     // 작업공정

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_CENTER", columnDefinition = "bigint COMMENT '작업장'")
    private WorkCenter workPlace;       // 작업장

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_TYPE", columnDefinition = "bigint COMMENT '검사유형'")
    private TestCategory testCategory;      // 검사유형

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RAW_MATERIAL_HOUSE", columnDefinition = "bigint COMMENT '원자재 창고'")
    private WareHouse rawMaterialWareHouse;        // 원자재 창고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INPUT_WAREHOUSE", columnDefinition = "bigint COMMENT '입고창고'")
    private WareHouse inputWareHouse;               // 입고 창고 (창고 참고)

    @Column(name = "IS_MAIN_PROCESS", columnDefinition = "bit(1) COMMENT '메인공정여부'")
    private boolean isMainProcess;          // 메인공정 (예 아니오)

    @Column(name = "IS_LAST_PROCESS", columnDefinition = "bit(1) COMMENT '최종공정여부'")
    private boolean isLastProcess;          // 최종공정 (예 아니고)

    @Column(name = "WORK_START_DATE", columnDefinition = "date COMMENT '작업개시일'")
    private LocalDate workStartDate;            // 작업개시일

    // 다대일 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTING", nullable = false, columnDefinition = "bigint COMMENT '라우팅'")
    private Routing routing;
}
