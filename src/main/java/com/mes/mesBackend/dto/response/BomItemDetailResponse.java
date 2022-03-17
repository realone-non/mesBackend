package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class BomItemDetailResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "레벨")
    int level;

    @Schema(description = "(품목) 고유아이디")
    Long itemId;

    @Schema(description = "(품목) 품번")
    String itemNo;

    @Schema(description = "(품목) 품명")
    String itemName;

    @Schema(description = "(품목) 품목 계정")
    String itemAccount;

    @Schema(description = "(품목) 제조사 품번")
    String itemManufacturerPartNo;

    @Schema(description = "(품목) 제조사")
    String itemClientName;      // 제조사

    @Schema(description = "(품목) 단위")
    String itemUnitCodeName;        // 재고단위

    @Schema(description = "(품목) 저장위치")
    String itemStorageLocation;    // 저장위치

    @Schema(description = "(품목) 입고단가")
    int itemInputUnitPrice;         // 입고단가

    @Schema(description = "수량")
    float amount;

    @Schema(description = "(거래처) 고유아이디")
    Long toBuyId;

    @Schema(description = "(거래처) 거래처명")
    String toBuyName;

    // 소수점 2자리
    @Schema(description = "금액")
    String price;      // 금액

    @Schema(description = "(공정) 고유아이디")
    Long workProcessId;

    @Schema(description = "(공정) 작업공정명")
    String workProcessName;     // 작업공정명

    @Schema(description = "사용")
    boolean useYn;

    @Schema(description = "비고")
    String note;
}
