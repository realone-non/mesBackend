package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class ItemResponse {
    Long id;
    String itemNo;      // 품번
    String itemName;    // 품명
    String standard;    // 규격
    ItemAccountResponse itemAccount;    // 품목계정
    ItemGroupResponse.idAndGroupName itemGroup;        // 품목그룹
    ItemFormResponse itemForm;          // 품목형태
    UseTypeResponse useType;            // 용도유형
    RoutingResponse.idAndName routing;            // 라우팅 (라우팅 명)
    UnitResponse.idAndName unit;        // 재고단위
    int uhp;            // uhp
    int validDay;       // 유효일수
    LotTypeResponse lotType;    // LOT유형
    String inputTest;               // 수입검사
    String outputTest;              // 출하검사
    boolean wasteProductLot;        // 폐기품 Lot 관리
    String developStatus;           // 개발상태
    boolean stockControl;       // 재고관리
    int inputUnitPrice;         // 입고단가
    String storageLocation;    // 저장위치
    String clientItemNo;        // 거래처 품번
    String manufacturerPartNo;        // 제조사품번
    ClientResponse.idAndName Manufacturer;      // 제조사
    TestCriteriaResponse testCriteria;      // 검사기준
    TestProcessResponse testProcess;        // 검사방법
    boolean useYn;                      // 사용
    String searchWord;          // 검색어
    boolean agingMaterialYn;      // 시효성자재
}
