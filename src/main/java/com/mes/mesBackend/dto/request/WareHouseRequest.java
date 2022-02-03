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
@Schema(description = "창고")
public class WareHouseRequest {
    @Schema(description = "창고코드")
    @NotBlank(message = NOT_EMPTY)
    String wareHouseCode;

    @Schema(description = "창고명")
    @NotBlank(message = NOT_EMPTY)
    String wareHouseName;

    @Schema(description = "창고유형 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long wareHouseType;

    @Schema(description = "창고그룹")
    String wareHouseGroup;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @Schema(description = "공정용 여부")
    @NotNull(message = NOT_NULL)
    boolean workProcessYn;
}
