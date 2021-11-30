package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "P/I")
@JsonInclude(NON_NULL)
public class EstimatePiResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "Invoice No")
    String invoiceNo;

    @Schema(description = "customer PO NO")
    String customerPoNo;

    @Schema(description = "Bill To")
    String billTo;

    @Schema(description = "Remarks")
    String remarks;
}
