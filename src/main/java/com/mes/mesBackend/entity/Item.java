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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "ITEM_NO", unique = true, nullable = false)
    private String itemNo;      // 품번

    @Column(name = "ITEM_NAME", nullable = false)
    private String itemName;    // 품명

    @Column(name = "STANDARD")
    private String standard;    // 규격

    // 일대다 단방향
    @OneToOne @JoinColumn(name = "ITEM_ACCOUNTS_ID")
    private ItemAccount itemAccount;    // 품목계정

    // 일대다 단방향
    @OneToOne @JoinColumn(name = "ITEM_GROUPS_ID")
    private ItemGroup itemGroup;        // 품목그룹

    @OneToOne @JoinColumn(name = "ITEM_FORMS_ID")
    private ItemForm itemForm;          // 품목형태

    @OneToOne @JoinColumn(name = "USE_TYPES_ID")
    private UseType useType;            // 용도유형

    @OneToOne @JoinColumn(name = "ROUTINGS_ID")
    private Routing routing;            // 라우팅 (라우팅 명)

    @OneToOne @JoinColumn(name = "UNIT_ID", nullable = false)
    private Unit unit;        // 재고단위

    @Column(name = "UHP", nullable = false)
    private int uhp;            // uhp

    @Column(name = "VALID_DAY", nullable = false)
    private int validDay;       // 유효일수

    @OneToOne @JoinColumn(name = "LOT_TYPES_ID", nullable = false)
    private LotType lotType;    // LOT유형

//    @OneToOne @JoinColumn(name = "INPUT_TEST")
//    private TestType inputTest;        // 수입검사
//
//    @OneToOne @JoinColumn(name = "PROCESS_TEST")
//    private TestType processTest;       // 공정검사
//
//    @OneToOne @JoinColumn(name = "SHIPMENT_TEST")
//    private TestType shipmentTest;      // 출하검사

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_CHECK_CATEGORY")
    private List<ItemCheckCategory> itemCheckCategory;

    @Column(name = "WASTE_PRODUCT_LOT", nullable = false)
    private boolean wasteProductLot;        // 폐기품 Lot 관리

    @Column(name = "DEVELOP_STATUS", nullable = false)
    private DevelopStatus developStatus = DevelopStatus.BEFORE;  // 개발상태

    @Column(name = "STOCK_CONTROL", nullable = false)
    private boolean stockControl;       // 재고관리

    @Column(name = "INPUT_UNIT_PRICE")
    private int inputUnitPrice;         // 입고단가

    @Column(name = "STORAGE_LOCATION")
    private String storageLocation;    // 저장위치

    @OneToOne @JoinColumn(name = "CLIENT_PART_NO")
    private Client clientPartNo;        // 거래처품번

    @Column(name = "MANUFACTURER_PART_NO")
    private String manufacturerPartNo;        // 제조사품번

    @Column(name = "MANUFACTURER")
    private String Manufacturer;            // 제조사

    @OneToOne @JoinColumn(name = "TEST_CRITERIAS_ID")
    private TestCriteria testCriteria;      // 검사기준

    @OneToOne @JoinColumn(name = "TEST_PROCESSES_ID")
    private TestProcess testProcess;        // 검사방법

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;                      // 사용

    @Column(name = "SEARCH_WORD")
    private String searchWord;          // 검색어

    @Column(name = "AGING_MATERIAL")
    private boolean agingMaterial;      // 시효성자재

    @OneToOne @JoinColumn(name = "ITEM_FILES_ID")
    private ItemFile itemFile;          // 파일
}