package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.Currency;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.enumeration.PayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "출하")
public class ShipmentRequest {
    @Schema(description = "거래처 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long client;

    @Schema(description = "출하일자")
    LocalDate shipmentDate;

    @Schema(description = "담당자 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long user;

    @Schema(description = "출하창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long wareHouse;

    @Schema(description = "화폐 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long currency;

    @Schema(description = "거래처담당자")
    String clientManager;

    @Schema(description = "결제조건")
    @NotNull(message = NOT_NULL)
    PayType payType;

    @Schema(description = "부가세적용")
    @NotNull(message = NOT_NULL)
    boolean surtax;

    @Schema(description = "운송조건")
    String transportCondition;

    @Schema(description = "Forwader")
    String forwader;

    @Schema(description = "비고")
    String note;
}
