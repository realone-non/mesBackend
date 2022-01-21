package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "불량항목")
public class BadItemRequest {

    @Schema(description = "불량항목코드")
    @NotBlank(message = NOT_NULL)
    String badItemCode;

    @Schema(description = "불량항목명")
    @NotBlank(message = NOT_NULL)
    String badItemName;

    @Schema(description = "순번")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn = true;

    @Schema(description = "작업공정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcessId;

    @Schema(description = "소진 원부자재")
    @NotBlank(message = NOT_NULL)
    String exhaustItem;
}
