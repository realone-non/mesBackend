package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "ITEM_NO", nullable = false, columnDefinition = "varchar(255) COMMENT '품번'")
    private String itemNo;      // 품번

    @Column(name = "ITEM_NAME",  nullable = false, columnDefinition = "varchar(255) COMMENT '품명'")
    private String itemName;    // 품명

    @Column(name = "STANDARD", columnDefinition = "varchar(255) COMMENT '규격'")
    private String standard;    // 규격

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT",  nullable = false, columnDefinition = "bigint COMMENT '품목계정'")
    private ItemAccount itemAccount;    // 품목계정

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_GROUP", columnDefinition = "bigint COMMENT '품목그룹'")
    private ItemGroup itemGroup ;        // 품목그룹

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_FORM", columnDefinition = "bigint COMMENT '품목형태'")
    private ItemForm itemForm = new ItemForm();          // 품목형태

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

    @Enumerated(EnumType.STRING)
    @Column(name = "INPUT_TEST", nullable = false,columnDefinition = "varchar(255) COMMENT '수입검사'")
    private TestType inputTest = TestType.NO_TEST;      // 수입검사

    @Enumerated(EnumType.STRING)
    @Column(name = "OUTPUT_TEST",nullable = false, columnDefinition = "varchar(255) COMMENT '출하검사'")
    private TestType outputTest = TestType.NO_TEST;     // 출하검사

    @Column(name = "WASTE_PRODUCT_LOT", nullable = false, columnDefinition = "bit(1) COMMENT '폐기품 LOT 관리'")
    private boolean wasteProductLot;        // 폐기품 Lot 관리

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVELOP_STATUS", nullable = false, columnDefinition = "varchar(255) COMMENT '개발상태'")
    private DevelopStatus developStatus = DevelopStatus.BEFORE;  // 개발상태

    @Column(name = "STOCK_CONTROL", nullable = false, columnDefinition = "bit(1) COMMENT '재고관리'")
    private boolean stockControl;       // 재고관리

    @Column(name = "INPUT_UNIT_PRICE", nullable = false,columnDefinition = "int COMMENT '입고단가'")
    private int inputUnitPrice;         // 입고단가

    @Column(name = "STORAGE_LOCATION", nullable = false, columnDefinition = "varchar(255) COMMENT '저장위치'")
    private String storageLocation;    // 저장위치

    @Column(name = "CLIENT_ITEM_NO", columnDefinition = "varchar(255) COMMENT '거래처품번'")
    private String clientItemNo;        // 거래처 품번

    @Column(name = "MANUFACTURER_PART_NO", nullable = false,columnDefinition = "varchar(255) COMMENT '제조사'")
    private String manufacturerPartNo;        // 제조사품번

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANUFACTURER", nullable = false,columnDefinition = "bigint COMMENT '제조사'")
    private Client manufacturer;        // 제조사

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

    @Column(name = "AGING_MATERIAL_YN", nullable = false, columnDefinition = "bit(1) COMMENT '시효정자재 여부'")
    private boolean agingMaterialYn;      // 시효성자재

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void addJoin(
            ItemAccount itemAccount,
            ItemGroup itemGroup,
            ItemForm itemForm,
            UseType useType,
            Routing routing,
            Unit unit,
            LotType lotType,
            Client  manufacturer,
            TestCriteria testCriteria,
            TestProcess testProcess
    ) {
        setItemAccount(itemAccount);
        setItemGroup(itemGroup);
        setItemForm(itemForm);
        setUseType(useType);
        setRouting(routing);
        setUnit(unit);
        setLotType(lotType);
        setManufacturer(manufacturer);
        setTestCriteria(testCriteria);
        setTestProcess(testProcess);
    }

    public void update(
            Item newItem,
            ItemAccount newItemAccount,
            ItemGroup newItemGroup,
            ItemForm newItemForm,
            UseType newUseType,
            Routing newRouting,
            Unit newUnit,
            LotType newLotType,
            Client newClient,
            TestCriteria newTestCriteria,
            TestProcess newTestProcess
    ) {
        setItemNo(newItem.itemNo);
        setItemName(newItem.itemName);
        setStandard(newItem.standard);
        setItemAccount(newItemAccount);
        setItemGroup(newItemGroup);
        setItemForm(newItemForm);
        setUseType(newUseType);
        setRouting(newRouting);
        setUnit(newUnit);
        setUhp(newItem.uhp);
        setValidDay(newItem.validDay);
        setLotType(newLotType);
        setInputTest(newItem.inputTest);
        setOutputTest(newItem.outputTest);
        setWasteProductLot(newItem.wasteProductLot);
        setDevelopStatus(newItem.developStatus);
        setStockControl(newItem.stockControl);
        setInputUnitPrice(newItem.inputUnitPrice);
        setStorageLocation(newItem.storageLocation);
        setClientItemNo(newItem.clientItemNo);
        setManufacturerPartNo(newItem.manufacturerPartNo);
        setManufacturer(newClient);
        setTestCriteria(newTestCriteria);
        setTestProcess(newTestProcess);
        setUseYn(newItem.useYn);
        setSearchWord(newItem.searchWord);
        setAgingMaterialYn(newItem.agingMaterialYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
