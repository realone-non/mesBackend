package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/*
 * 7-1. Lot 마스터 조회
 * 검색: 공장,품목그룹,품목,LOT번호,창고,등록유형,재고유무,LOT유형,검사증여부,유효여부
 * 공장            -> Factory
 * 품번            -> Item
 * 품명            -> Item
 * 창고            -> WareHouse
 * LOT 번호
 * 시리얼번호
 * LOT 유형         -> LotType
 * 등록유형          -> EnrollmentType
 * 재고수량
 * 생성수량
 * 불량수량
 * 투입수량
 * 전환수량
 * 이동수량
 * 실사수량
 * 출하수량
 * 반품수량
 * 검사요청수량
 * 검사수량
 * 품질등급             -> QualityLevel
 * 생성일시             -> BaseTimeEntity
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "LOT_MASTERS")
@Data
public class LotMaster extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'LOT마스터 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM",columnDefinition = "bigint COMMENT '품목'")
    private Item item;      // 품목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;        // 공장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;        // 창고

    @Column(name = "LOT_NO", columnDefinition = "varchar(255) COMMENT 'LOT 번호'")
    private String lotNo;       // LOT 번호

    @Column(name = "SERIAL_NO", columnDefinition = "varchar(255) COMMENT '시리얼 번호'")
    private String serialNo;        // 시리얼번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_TYPE", columnDefinition = "bigint COMMENT 'LOT 유형'")
    private LotType lotType;        // LOT 유형

    @Enumerated(EnumType.STRING)
    @Column(name = "ENROLLMENT_TYPE", columnDefinition = "varchar(255) COMMENT '등록유형'")
    private EnrollmentType enrollmentType;      // 등록유형

    @Column(name = "CREATED_AMOUNT", columnDefinition = "int COMMENT '생산수량'")
    private int createdAmount;       // 생산수량

    @Column(name = "BAD_ITEM_AMOUNT", columnDefinition = "int COMMENT '불량수량'")
    private int badItemAmount;      // 불량수량

    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '투입수량'")
    private int inputAmount;        // 투입수량

    @Column(name = "CHANGED_AMOUNT", columnDefinition = "int COMMENT '전환수량'")
    private int changeAmount;       // 전환수량

    @Column(name = "TRANSFER_AMOUNT", columnDefinition = "int COMMENT '수량'")
    private int transferAmount;     // 이동수량

    @Column(name = "INSPECT_AMOUNT", columnDefinition = "int COMMENT '실사수량'")
    private int inspectAmount;      // 실사수량

    @Column(name = "SHIPMENT_AMOUNT", columnDefinition = "int COMMENT '출하수량'")
    private int shipmentAmount;     // 출하수량

    @Column(name = "RETURN_AMOUNT", columnDefinition = "int COMMENT '반품수량'")
    private int returnAmount;       // 반품수량

    @Column(name = "CHECK_REQUEST_AMOUNT", columnDefinition = "int COMMENT '검사요청수량'")
    private int checkRequestAmount;     // 검사요청수량

    @Column(name = "CHECK_AMOUNT", columnDefinition = "int COMMENT '검사수량'")
    private int checkAmount;            // 검사수량

    @Enumerated(EnumType.STRING)
    @Column(name = "QUALITY_LEVEL", columnDefinition = "varchar(255) COMMENT '품질등급'")
    private QualityLevel qualityLevel;      // 품질등급
}