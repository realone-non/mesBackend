package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


/* 라우팅 상세 등록
 * 작업순번, 작업공정, 작업장, 검사유형, 원자재창고, 입고창고, 메인공정, 최종공정, 작업개시일, 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ROUTING_DETAILS")
@Data
public class RoutingDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "ORDERS")
    private int orders;     // 작업순번

    @Column(name = "WORK_PROCESS")
    private String workProcess;     // 작업공정

    @Column(name = "WORK_PLACE")
    private String workPlace;       // 작업장

    @OneToOne @JoinColumn(name = "TEST_TYPES_ID")
    private TestType testType;      // 검사유형

    @OneToOne @JoinColumn(name = "RAW_MATERIAL_HOUSE")
    private WareHouse rawMaterialWareHouse;        // 원자재 창고 (창고 참조)

    @OneToOne @JoinColumn(name = "INPUT_WAREHOUSE")
    private WareHouse inputWareHouse;               // 입고 창고 (창고 참고)

    @Column(name = "IS_MAIN_PROCESS")
    private boolean isMainProcess;          // 메인공정 (예 아니오)

    @Column(name = "IS_LAST_PROCESS")
    private boolean isLastProcess;          // 최종공정 (예 아니고)

    @Column(name = "WORK_START_DATE")
    private LocalDate workStartDate;            // 작업개시일
}
