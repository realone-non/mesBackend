package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.ProductionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "수주")
public class ContractRequest {
    @Schema(description = "고유아이디")
    Long client;

    @Schema(description = "수주일자")
    @NotNull(message = NOT_NULL)
    LocalDate contractDate;

    @Schema(description = "고객발주일자")
    @NotNull(message = NOT_NULL)
    LocalDate clientOrderDate;

    @Schema(description = "생산유형")
    @NotNull(message = NOT_NULL)
    ProductionType productionType = ProductionType.MASS;

    @Schema(description = "고객발주번호")
    @NotBlank(message = NOT_EMPTY)
    String clientOrderNo;

    @Schema(description = "직원 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long user;

    @Schema(description = "화폐 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long currency;

    @Schema(description = "부가세적용")
    @NotBlank(message = NOT_EMPTY)
    String surtax;

    @Schema(description = "창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long outputWareHouse;

    @Schema(description = "납기일자")
    @NotNull(message = NOT_NULL)
    LocalDate periodDate;

    @Schema(description = "변경사유")
    String changeReason;

    @Schema(description = "결제완료")
    @NotNull(message = NOT_NULL)
    boolean paymentYn;

    @Schema(description = "지불조건")
    @NotBlank(message = NOT_EMPTY)
    String payCondition;

    @Schema(description = "Forwader")
    @NotBlank(message = NOT_EMPTY)
    String forwader;

    @Schema(description = "운송조건")
    @NotBlank(message = NOT_EMPTY)
    String transportCondition;

    @Schema(description = "Shipment Service")
    @NotBlank(message = NOT_EMPTY)
    String shipmentService;

    @Schema(description = "Shipment WK")
    @NotBlank(message = NOT_EMPTY)
    String shipmentWk;

    @Schema(description = "비고")
    String note;
}
