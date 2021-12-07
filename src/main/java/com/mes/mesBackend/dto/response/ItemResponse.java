package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "품목")
public class ItemResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품번")
    String itemNo;      // 품번

    @Schema(description = "품명")
    String itemName;    // 품명

    @Schema(description = "규격")
    String standard;    // 규격

    @Schema(description = "품목계정")
    ItemAccountResponse itemAccount;    // 품목계정

    @Schema(description = "품목그룹")
    ItemGroupResponse.idAndGroupName itemGroup;        // 품목그룹

    @Schema(description = "품목형태")
    ItemFormResponse itemForm;          // 품목형태

    @Schema(description = "용도유형")
    UseTypeResponse useType;            // 용도유형

    @Schema(description = " 라우팅")
    RoutingResponse.idAndName routing;            // 라우팅 (라우팅 명)

    @Schema(description = "재고단위")
    UnitResponse.idAndName unit;        // 재고단위

    @Schema(description = "uhp")
    int uhp;            // uhp

    @Schema(description = "유효일수")
    int validDay;       // 유효일수

    @Schema(description = "LOT유형")
    LotTypeResponse lotType;    // LOT유형

    @Schema(description = "수입검사")
    String inputTest;               // 수입검사

    @Schema(description = "출하검사")
    String outputTest;              // 출하검사

    @Schema(description = "폐기품 Lot 관리")
    boolean wasteProductLot;        // 폐기품 Lot 관리

    @Schema(description = "개발상태")
    String developStatus;           // 개발상태

    @Schema(description = "재고관리")
    boolean stockControl;       // 재고관리

    @Schema(description = "입고단가")
    int inputUnitPrice;         // 입고단가

    @Schema(description = "저장위치")
    String storageLocation;    // 저장위치

    @Schema(description = "거래처품번")
    String clientItemNo;        // 거래처 품번

    @Schema(description = "제조사품번")
    String manufacturerPartNo;        // 제조사품번

    @Schema(description = "제조사")
    ClientResponse.idAndName Manufacturer;      // 제조사

    @Schema(description = "검사기준")
    TestCriteriaResponse testCriteria;      // 검사기준

    @Schema(description = "검사방법")
    TestProcessResponse testProcess;        // 검사방법

    @Schema(description = "사용여부")
    boolean useYn;                      // 사용

    @Schema(description = "검색어")
    String searchWord;          // 검색어

    @Schema(description = "시효성자재")
    boolean agingMaterialYn;      // 시효성자재

    @Getter
    @Setter
    @JsonInclude(NON_NULL)
    public static class itemToBomResponse {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "품번")
        String itemNo;

        @Schema(description = "품명")
        String itemName;

        @Schema(description = "품목 계정")
        String itemAccount;

        @Schema(description = "제조사 품번")
        String manufacturerPartNo;

        @Schema(description = "제조사")
        String clientName;      // 제조사

        @Schema(description = "단위")
        String unitCodeName;        // 재고단위

        @Schema(description = "저장위치")
        String storageLocation;    // 저장위치

        @Schema(description = "입고단가")
        int inputUnitPrice;         // 입고단가
    }

    @Getter
    @Setter
    public static class noAndName {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "품번")
        String itemNo;

        @Schema(description = "품명")
        String itemName;
    }

    @Getter
    @Setter
    @JsonInclude(NON_NULL)
    @Schema(description = "품목")
    public static class noAndNameAndStandardAndUnitAndPrice {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "품번")
        String itemNo;

        @Schema(description = "품명")
        String itemName;

        @Schema(description = "규격")
        String standard;    // 규격

        @Schema(description = "단위")
        String unitCodeName;

        @Schema(description = "입고단가")
        int inputUnitPrice;
    }


}
