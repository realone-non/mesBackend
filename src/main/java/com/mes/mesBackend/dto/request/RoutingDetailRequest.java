package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "라우팅 상세")
public class RoutingDetailRequest {
    @Schema(description = "작업순번")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;

    @Schema(description = "작업공정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workProcess;

    @Schema(description = "작업장 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long workCenter;

    @Schema(description = "검사유형")
    TestCategory testCategory = TestCategory.INPUT_TEST;

    @Schema(description = "원자재 창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long rawMaterialWareHouse;

    @Schema(description = "입고 창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long inputWareHouse;

    @Schema(description = "메인공정")
    @NotNull(message = NOT_NULL)
    boolean mainProcessYn;

    @Schema(description = "최종공정")
    @NotNull(message = NOT_NULL)
    boolean lastProcessYn;

    @Schema(description = "작업개시일")
    @NotNull(message = NOT_NULL)
    LocalDate workStartDate;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
