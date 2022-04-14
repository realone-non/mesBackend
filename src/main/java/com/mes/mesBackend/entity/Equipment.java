package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

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
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "EQUIPMENTS")
@Data
public class Equipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비등록 고유아이디'")
    private Long id;

    @Column(name = "EQUIPMENT_CODE", columnDefinition = "varchar(255) COMMENT '설비코드'")
    private String equipmentCode;        // 설비코드

    @Column(name = "EQUIPMENT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '설비명'")
    private String equipmentName;        // 설비명

    @Column(name = "MODEL", columnDefinition = "varchar(255) COMMENT '규격모델'")
    private String model;               // 규격&모델

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '구매처'")
    private Client client;

    @Column(name = "PURCHASE_DATE", columnDefinition = "date COMMENT '구매일자'")
    private LocalDate purchaseDate;        // 구매일자

    @Column(name = "PURCHASE_AMOUNT", columnDefinition = "int COMMENT '구입금액'")
    private int purchaseAmount;      // 구입금액

    @Column(name = "MAKER", columnDefinition = "varchar(255) COMMENT '생산업체명'")
    private String maker;               // 생산업체명

    @Column(name = "SERIAL_NO", columnDefinition = "varchar(255) COMMENT '시리얼번호'")
    private String serialNo;            // 시리얼번호

    @Column(name = "START_DATE", columnDefinition = "date COMMENT '생산개시일자'")
    private LocalDate startDate;        // 생산개시일자

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_LINE", columnDefinition = "bigint COMMENT '작업라인,설비유형'")
    private WorkLine workLine;              // 작업라인,설비유형

    @Column(name = "CHECK_CYCLE", columnDefinition = "int COMMENT '점검주기'")
    private int checkCycle;             // 점검주기

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    // 지금은 일단 설비 등록할 때 등록 한 작업공정으로 보여주기로
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;

    @Column(name = "LIFE", columnDefinition = "varchar(255) COMMENT '수명(월)정보'")
    private String life;

    @Column(name = "LAST_TEST_DATE", columnDefinition = "date COMMENT '최종점검일자'")
    private LocalDate lastTestDate;

    @Column(name = "PRODUCE_YN", columnDefinition = "bit(1) COMMENT '원료혼합 공정 반제품 생성 가능 여부'", nullable = false)
    private boolean produceYn;  // true: 생성가능, false: 생성 불가능

    @Column(name = "LOT_CODE", columnDefinition = "varchar(255) COMMENT '로트생성 설비 코드'")
    private String lotCode;

    public void addJoin(Client client, WorkLine workLine, WorkProcess workProcess) {
        setClient(client);
        setWorkLine(workLine);
        setWorkProcess(workProcess);
        setProduceYn(true);
    }

    public void update(Equipment newEquipment, Client newClient, WorkLine newWorkLine, WorkProcess workProcess) {
        setEquipmentCode(newEquipment.equipmentCode);
        setEquipmentName(newEquipment.equipmentName);
        setModel(newEquipment.model);
        setClient(newClient);
        setPurchaseDate(newEquipment.purchaseDate);
        setPurchaseAmount(newEquipment.purchaseAmount);
        setMaker(newEquipment.maker);
        setSerialNo(newEquipment.serialNo);
        setStartDate(newEquipment.startDate);
        setWorkLine(newWorkLine);
        setCheckCycle(newEquipment.checkCycle);
        setUseYn(newEquipment.useYn);
        setWorkProcess(workProcess);
        setLife(newEquipment.life);
        setLastTestDate(newEquipment.lastTestDate);
        setLotCode(newEquipment.lotCode);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
