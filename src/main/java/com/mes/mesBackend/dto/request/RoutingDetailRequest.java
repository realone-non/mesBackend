package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.ONE_VALUE;
import static com.mes.mesBackend.exception.Message.NOT_ZERO;

@Getter
@Setter
@Schema(description = "라우팅 상세")
public class RoutingDetailRequest {
    @Schema(description = "작업순번")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    int orders;

    @Schema(description = "작업공정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long workProcess;

    @Schema(description = "작업장 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long workCenter;

    @Schema(description = "검사유형")
    TestCategory testCategory = TestCategory.INPUT_TEST;

    @Schema(description = "원자재 창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long rawMaterialWareHouse;

    @Schema(description = "입고 창고 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long inputWareHouse;

    @Schema(description = "메인공정")
    boolean mainProcessYn;

    @Schema(description = "최종공정")
    boolean lastProcessYn;

    @Schema(description = "작업개시일")
    LocalDate workStartDate;

    @Schema(description = "사용여부")
    boolean useYn;
}
