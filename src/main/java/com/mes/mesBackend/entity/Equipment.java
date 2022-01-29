package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "EQUIPMENT_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '설비코드'")
    private String equipmentCode;        // 설비코드

    @Column(name = "EQUIPMENT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '설비명'")
    private String equipmentName;        // 설비명

    @Column(name = "MODEL", columnDefinition = "varchar(255) COMMENT '규격모델'", nullable = false)
    private String model;               // 규격&모델

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '구매처'", nullable = false)
    private Client client;

    @Column(name = "PURCHASE_DATE", columnDefinition = "datetime(6) COMMENT '구매일자'", nullable = false)
    private LocalDateTime purchaseDate;        // 구매일자

    @Column(name = "PURCHASE_AMOUNT", columnDefinition = "int COMMENT '구입금액'")
    private int purchaseAmount;      // 구입금액

    @Column(name = "MAKER", columnDefinition = "varchar(255) COMMENT '생산업체명'", nullable = false)
    private String maker;               // 생산업체명

    @Column(name = "SERIAL_NO", columnDefinition = "varchar(255) COMMENT '시리얼번호'")
    private String serialNo;            // 시리얼번호

    @Column(name = "START_DATE", columnDefinition = "datetime(6) COMMENT '생산개시일자'", nullable = false)
    private LocalDateTime startDate;        // 생산개시일자

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_LINE", columnDefinition = "bigint COMMENT '작업라인,설비유형'", nullable = false)
    private WorkLine workLine;              // 작업라인,설비유형

    @Column(name = "CHECK_CYCLE", columnDefinition = "int COMMENT '점검주기'", nullable = false)
    private int checkCycle;             // 점검주기

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'", nullable = false)
    private WorkProcess workProcess;

    public void addJoin(Client client, WorkLine workLine, WorkProcess workProcess) {
        setClient(client);
        setWorkLine(workLine);
        setWorkProcess(workProcess);
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
    }

    public void delete() {
        setDeleteYn(true);
    }
}
