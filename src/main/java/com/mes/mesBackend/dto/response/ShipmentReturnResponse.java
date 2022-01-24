package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.LotType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 4-6. 출하반품 등록
@Getter
@Setter
@Schema(description = "출하 반품")
@JsonInclude(NON_NULL)
public class ShipmentReturnResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "출하번호")
    String shipmentNo;

    @Schema(description = "거래처")
    String clientCode;

    @Schema(description = "거래처명")
    String clientName;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String itemStandard;

    @Schema(description = "LOT 고유아이디")
    Long lotMasterId;

    @Schema(description = "LOT 번호")
    String lotNo;

    @Schema(description = "LOT 유형")
    String lotType;

    @Schema(description = "반품일시")
    LocalDate returnDate;

    @Schema(description = "반품수량")
    int returnAmount;

    @Schema(description = "출하수량")
    int shipmentAmount;

    @Schema(description = "입고창고 고유아이디")
    Long warehouseId;

    @Schema(description = "입고창고")
    String warehouseName;

    @Schema(description = "비고")
    String note;
}
