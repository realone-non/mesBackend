package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "수주 품목")
public class ContractItemRequest {
    @Schema(description = "품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long item;      // 품번, 품명, 규격, 수주단위, 단가

    @Schema(description = "수주수량")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int amount;     // 수주수량

    @Schema(description = "수주유형 [DIFFUSION: 방산 , DOMESTIC: 국내, OVERSEAS: 해외 , ODM: ODM]")
    @NotNull(message = NOT_NULL)
    ContractType contractType;

    @Schema(description = "납기일자")
    @NotNull(message = NOT_NULL)
    LocalDate periodDate;       // 납기일자

    @Schema(description = "규격화 품번")
    String standardItemNo;      // 규격화 품번

    @Schema(description = "비고")
    String note;        // 비고
}
