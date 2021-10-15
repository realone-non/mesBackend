package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목등록
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

    @OneToOne @JoinColumn(name = "STOCK_UNITS_ID", nullable = false)
    private StockUnit stockUnit;        // 재고단위

    @Column(name = "UHP", nullable = false)
    private int uhp;            // uhp

    @Column(name = "VALID_DAY", nullable = false)
    private int validDay;       // 유효일수

    @OneToOne @JoinColumn(name = "LOT_TYPES_ID", nullable = false)
    private LotType lotType;    // LOT유형

    @OneToOne @JoinColumn(name = "INPUT_TEST")
    private TestType inputTest;        // 수입검사

    @OneToOne @JoinColumn(name = "PROCESS_TEST")
    private TestType processTest;       // 공정검사

    @OneToOne @JoinColumn(name = "SHIPMENT_TEST")
    private TestType shipmentTest;      // 출하검사

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

    @Column(name = "CLIENT_PART_NO")
    private String clientPartNo;        // 거래처품번

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
