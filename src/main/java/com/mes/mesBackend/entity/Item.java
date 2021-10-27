package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// 품목등록
/*
* 품목등록
* 검색: 품목그룹(체크박스),품목계정(체크),품번(텍스트),품명(텍스트),검색어(텍스트)
* 품번 (AA01-AF2-E001DB)
* 품명 (EMI FILTER)
* 규격 (AF2-E001DB)
* 품목계정 (제품,반재료,부재료)     -> ItemAccount
* 품목그룹 (필터,계측기,기타)       -> ItemGroup
* 품목형태 (부자재,기구,부품)       -> ItemForm
* 용도유형 (방산제,계측기)         -> UseType
* 라우팅 (필터생산,필터개발)        -> Routing
* 재고단위 (개,그램,봉지)          ->  Unit
* UPH (1)
* 유효일수 (365)
* LOT유형 (파렛트)               -> LotType
* 수입검사 (자동검사,수동검사)      -> ItemCheckCategory
* 공정검사 (자동검사,수동검사)       -> ItemCheckCategory
* 출하검사 (자동검사,수동검사)       -> ItemCheckCategory
* 폐기품 LOT관리 (예,아니오)
* 개발상태 (개발중,개발완료)         -> DevelopStatus
* 재고관리 (예,아니오)
* 입고단가 (금액 숫자)
* 저장위치 (A10,A3-1b)          -> 미구현
* 거래처 품번 (A-MB2-C010A-2,계측기 EA-2100)            -> Client
* 제조사품번 (A-MB2-C010A-2,Switch-Power;ALPS SDDF-3 250V 5A)
* 제조사 (EMCIS,성지전자)
* 검사기준 (품목확인)           -> TestCriteria
* 검사방법 (샘플,전수)          -> TestProcess
* 사용
* 검색어 (대원기전,'전해 NXL 바인텔레콤 NC2-A013A 1500uF ±20% / 50V,NXL50VB1500M7.5TP18X31.5)
* 시효성자재 (예,아니오)
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEMS")
@Data
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목 고유아이디'")
    private Long id;

    @Column(name = "ITEM_NO", unique = true, nullable = false, columnDefinition = "varchar(255) COMMENT '품번'")
    private String itemNo;      // 품번

    @Column(name = "ITEM_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '품명'")
    private String itemName;    // 품명

    @Column(name = "STANDARD", columnDefinition = "varchar(255) COMMENT '규격'")
    private String standard;    // 규격

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT", columnDefinition = "bigint COMMENT '품목계정'")
    private ItemAccount itemAccount;    // 품목계정

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_GROUP", columnDefinition = "bigint COMMENT '품목그룹'")
    private ItemGroup itemGroup;        // 품목그룹

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_FORM", columnDefinition = "bigint COMMENT '품목형태'")
    private ItemForm itemForm;          // 품목형태

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USE_TYPE", columnDefinition = "bigint COMMENT '용도유형'")
    private UseType useType;            // 용도유형

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTING", columnDefinition = "bigint COMMENT '라우팅'")
    private Routing routing;            // 라우팅 (라우팅 명)

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", nullable = false, columnDefinition = "bigint COMMENT '재고단위'")
    private Unit unit;        // 재고단위

    @Column(name = "UHP", nullable = false, columnDefinition = "int COMMENT 'uhp'")
    private int uhp;            // uhp

    @Column(name = "VALID_DAY", nullable = false, columnDefinition = "int COMMENT '유효일수'")
    private int validDay;       // 유효일수

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_TYPES_ID", nullable = false, columnDefinition = "bigint COMMENT 'LOT유형'")
    private LotType lotType;    // LOT유형

//    @OneToOne @JoinColumn(name = "INPUT_TEST", columnDefinition = "varchar(255) COMMENT ''")
//    private TestType inputTest;        // 수입검사
//
//    @OneToOne @JoinColumn(name = "PROCESS_TEST")
//    private TestType processTest;       // 공정검사
//
//    @OneToOne @JoinColumn(name = "SHIPMENT_TEST")
//    private TestType shipmentTest;      // 출하검사

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_CHECK_CATEGORY", columnDefinition = "bigint COMMENT '품목별 검사항목'")
    private List<ItemCheckCategory> itemCheckCategory;

    @Column(name = "WASTE_PRODUCT_LOT", nullable = false, columnDefinition = "bit(1) COMMENT '폐기품 LOT 관리'")
    private boolean wasteProductLot;        // 폐기품 Lot 관리

    @Column(name = "DEVELOP_STATUS", nullable = false, columnDefinition = "bigint COMMENT '개발상태'")
    private DevelopStatus developStatus = DevelopStatus.BEFORE;  // 개발상태

    @Column(name = "STOCK_CONTROL", nullable = false, columnDefinition = "bit(1) COMMENT '재고관리'")
    private boolean stockControl;       // 재고관리

    @Column(name = "INPUT_UNIT_PRICE", columnDefinition = "int COMMENT '입고단가'")
    private int inputUnitPrice;         // 입고단가

    @Column(name = "STORAGE_LOCATION", columnDefinition = "varchar(255) COMMENT '저장위치'")
    private String storageLocation;    // 저장위치

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'")
    private Client clientPartNo;        // 거래처

    @Column(name = "MANUFACTURER_PART_NO", columnDefinition = "varchar(255) COMMENT '제조사'")
    private String manufacturerPartNo;        // 제조사품번

    @Column(name = "MANUFACTURER", columnDefinition = "varchar(255) COMMENT '제조사'")
    private String Manufacturer;            // 제조사

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_CRITERIA", columnDefinition = "bigint COMMENT '검사기준'")
    private TestCriteria testCriteria;      // 검사기준

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_PROCESS", columnDefinition = "bigint COMMENT '검사방법'")
    private TestProcess testProcess;        // 검사방법

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;                      // 사용

    @Column(name = "SEARCH_WORD", columnDefinition = "varchar(255) COMMENT '검색어'")
    private String searchWord;          // 검색어

    @Column(name = "AGING_MATERIAL_YN", columnDefinition = "bit(1) COMMENT '시효정자재 여부'")
    private boolean agingMaterialYn;      // 시효성자재

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_FILE", columnDefinition = "bigint COMMENT '파일'")
    private ItemFile itemFile;          // 파일

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
