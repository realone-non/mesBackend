package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.service.ItemAccountCodeService;
import com.querydsl.core.annotations.QueryInit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

// 3-2-1. 품목등록
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "ITEMS")
@Data
@Table(indexes = {
        @Index(name = "item_no", columnList = "ITEM_NO"),
        @Index(name = "item_name", columnList = "ITEM_NAME")
})
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목 고유아이디'")
    private Long id;

    @Column(name = "ITEM_NO", nullable = false, columnDefinition = "varchar(255) COMMENT '품번'")
    private String itemNo;      // 품번

    @Column(name = "ITEM_NAME",  nullable = false, columnDefinition = "varchar(255) COMMENT '품명'")
    private String itemName;    // 품명

    @Column(name = "STANDARD", columnDefinition = "varchar(255) COMMENT '규격'")
    private String standard;    // 규격

    // 다대일 단방향
    @QueryInit("*.*")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT",  nullable = false, columnDefinition = "bigint COMMENT '품목계정'")
    private ItemAccount itemAccount;    // 품목계정

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_GROUP", columnDefinition = "bigint COMMENT '품목그룹'")
    private ItemGroup itemGroup ;        // 품목그룹

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_FORM", columnDefinition = "bigint COMMENT '품목형태'")
    private ItemForm itemForm = new ItemForm();          // 품목형태

    // 다대일 단방향
    // TODO: 삭제
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USE_TYPE", columnDefinition = "bigint COMMENT '용도유형'")
    private UseType useType;            // 용도유형

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ROUTING", columnDefinition = "bigint COMMENT '라우팅'")
    private Routing routing;            // 라우팅 (라우팅 명)

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UNIT", nullable = false, columnDefinition = "bigint COMMENT '재고단위'")
    private Unit unit;        // 재고단위

    @Column(name = "UHP", nullable = false, columnDefinition = "int COMMENT 'uhp'")
    private int uhp;            // uhp

    @Column(name = "VALID_DAY", nullable = false, columnDefinition = "int COMMENT '유효일수'")
    private int validDay;       // 유효일수

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_TYPES_ID", nullable = false, columnDefinition = "bigint COMMENT 'LOT유형'")
    private LotType lotType;    // LOT유형

    @Column(name = "WASTE_PRODUCT_LOT", nullable = false, columnDefinition = "bit(1) COMMENT '폐기품 LOT 관리'")
    private boolean wasteProductLot;        // 폐기품 Lot 관리

    @Enumerated(STRING)
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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MANUFACTURER", nullable = false,columnDefinition = "bigint COMMENT '제조사'")
    private Client manufacturer;        // 제조사

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEST_CRITERIA", columnDefinition = "bigint COMMENT '검사기준'")
    private TestCriteria testCriteria;      // 검사기준

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;                      // 사용

    @Column(name = "SEARCH_WORD", columnDefinition = "varchar(255) COMMENT '검색어'")
    private String searchWord;          // 검색어

    @Column(name = "AGING_MATERIAL_YN", nullable = false, columnDefinition = "bit(1) COMMENT '시효정자재 여부'")
    private boolean agingMaterialYn;      // 시효성자재

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT_CODE", columnDefinition = "bigint COMMENT '품목계정코드'")
    private ItemAccountCode itemAccountCode;

    @Enumerated(STRING)
    @Column(name = "TEST_CATEGORY", columnDefinition = "varchar(255) COMMENT '검사종류'")
    private TestCategory testCategory;      // 검사종류: ex) 수입검사, 공정검사, 출하검사

    @Enumerated(STRING)
    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '검사유형'")
    private TestType testType;              // 검사유형: ex) 자동검사, 수동검사, 검사없음

    @Enumerated(STRING)
    @Column(name = "INSPECTION_TYPE", columnDefinition = "varchar(255) COMMENT '검사방법'")
    private InspectionType inspectionType;  // 검사방법: ex) Sampling, 전수

    public void mapping(
            ItemAccount itemAccount,
            ItemGroup itemGroup,
            ItemForm itemForm,
            UseType useType,
            Routing routing,
            Unit unit,
            LotType lotType,
            Client  manufacturer,
            TestCriteria testCriteria,
            ItemAccountCode itemAccountCode
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
        setItemAccountCode(itemAccountCode);
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
            ItemAccountCode newItemAccountCode
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
        setWasteProductLot(newItem.wasteProductLot);
        setDevelopStatus(newItem.developStatus);
        setStockControl(newItem.stockControl);
        setInputUnitPrice(newItem.inputUnitPrice);
        setStorageLocation(newItem.storageLocation);
        setClientItemNo(newItem.clientItemNo);
        setManufacturerPartNo(newItem.manufacturerPartNo);
        setManufacturer(newClient);
        setTestCriteria(newTestCriteria);
        setUseYn(newItem.useYn);
        setSearchWord(newItem.searchWord);
        setAgingMaterialYn(newItem.agingMaterialYn);
        setItemAccountCode(newItemAccountCode);
        setTestCategory(newItem.testCategory);
        setTestType(newItem.testType);
        setInspectionType(newItem.inspectionType);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
