package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "품목")
public class ItemResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "품번")
    String itemNo;      // 품번

    @ApiModelProperty(value = "품명")
    String itemName;    // 품명

    @ApiModelProperty(value = "규격")
    String standard;    // 규격

    @ApiModelProperty(value = "품목계정")
    ItemAccountResponse itemAccount;    // 품목계정

    @ApiModelProperty(value = "품목그룹")
    ItemGroupResponse.idAndGroupName itemGroup;        // 품목그룹

    @ApiModelProperty(value = "품목형태")
    ItemFormResponse itemForm;          // 품목형태

    @ApiModelProperty(value = "용도유형")
    UseTypeResponse useType;            // 용도유형

    @ApiModelProperty(value = " 라우팅")
    RoutingResponse.idAndName routing;            // 라우팅 (라우팅 명)

    @ApiModelProperty(value = "재고단위")
    UnitResponse.idAndName unit;        // 재고단위

    @ApiModelProperty(value = "uhp")
    int uhp;            // uhp

    @ApiModelProperty(value = "유효일수")
    int validDay;       // 유효일수

    @ApiModelProperty(value = "LOT유형")
    LotTypeResponse lotType;    // LOT유형

    @ApiModelProperty(value = "수입검사")
    String inputTest;               // 수입검사

    @ApiModelProperty(value = "출하검사")
    String outputTest;              // 출하검사

    @ApiModelProperty(value = "폐기품 Lot 관리")
    boolean wasteProductLot;        // 폐기품 Lot 관리

    @ApiModelProperty(value = "개발상태")
    String developStatus;           // 개발상태

    @ApiModelProperty(value = "재고관리")
    boolean stockControl;       // 재고관리

    @ApiModelProperty(value = "입고단가")
    int inputUnitPrice;         // 입고단가

    @ApiModelProperty(value = "저장위치")
    String storageLocation;    // 저장위치

    @ApiModelProperty(value = "거래처품번")
    String clientItemNo;        // 거래처 품번

    @ApiModelProperty(value = "제조사품번")
    String manufacturerPartNo;        // 제조사품번

    @ApiModelProperty(value = "제조사")
    ClientResponse.idAndName Manufacturer;      // 제조사

    @ApiModelProperty(value = "검사기준")
    TestCriteriaResponse testCriteria;      // 검사기준

    @ApiModelProperty(value = "검사방법")
    TestProcessResponse testProcess;        // 검사방법

    @ApiModelProperty(value = "사용여부")
    boolean useYn;                      // 사용

    @ApiModelProperty(value = "검색어")
    String searchWord;          // 검색어

    @ApiModelProperty(value = "시효성자재")
    boolean agingMaterialYn;      // 시효성자재
}
